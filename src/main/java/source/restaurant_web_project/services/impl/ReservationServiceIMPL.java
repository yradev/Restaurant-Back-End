package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.models.builders.ReservationBuilder.ReservationBuilder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.ReservationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceIMPL implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final ReservationBuilder reservationBuilder;

    public ReservationServiceIMPL(ReservationRepository reservationRepository, UserRepository userRepository, ModelMapper modelMapper, ReservationBuilder reservationBuilder) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.reservationBuilder = reservationBuilder;
    }

    @Override
    public void reserve(NewReservationDTO newReservationDTO) {

        Reservation reservation = reservationBuilder.buildReservation()
                .mapToClass(newReservationDTO)
                .withUser(userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .withStatus(ReservationStatus.PENDING)
                .withReservationAt(LocalDateTime.now())
                .build();

        reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public ReservationViewDTO getActiveReservation() {
        ReservationViewDTO reservation = reservationRepository.findReservationsByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()).stream()
                .filter(a -> a.getStatus() != ReservationStatus.HISTORY)
                .map(tempReservation -> reservationBuilder.buildReservationView()
                        .mapToClass(tempReservation)
                        .build())
                .findFirst()
                .orElseThrow(NotFoundException::new);

        if (LocalDateTime.parse(reservation.getReservationFor()).isBefore(LocalDateTime.now())) {
            this.changeReservationStatus(reservation.getId(), ReservationStatus.HISTORY.name());
            return null;
        }

        return reservation;
    }

    @Override
    public List<ReservationViewDTO> getStaffActiveReservations() {
        return reservationRepository.findAll().stream()
                .filter(a -> a.getStatus() != ReservationStatus.HISTORY)
                .map(reservation ->
                        reservationBuilder.buildReservationView()
                                .mapToClass(reservation)
                                .withUserUserName(reservation.getUser().getEmail())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public void changeReservationStatus(long reservationId, String status) {
        Reservation currReservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(NotFoundException::new);

        Reservation reservation = reservationBuilder.buildReservation()
                .cloneObject(currReservation)
                .withStatus(ReservationStatus.valueOf(status))
                .build();

        reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public void cancelReservation(long id) {
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Reservation reservation = reservationRepository.findReservationById(id);

        if (reservation.getUser() != user) {
            throw new BadRequestException("You cant cancel reservation of another user!");
        }

        reservationRepository.delete(reservation);
    }

    @Override
    public List<ReservationViewDTO> getHistoryForUser() {
        return reservationRepository.findReservationsByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()).stream()
                .sorted((a, b) -> b.getReservationAt().compareTo(a.getReservationAt()))
                .filter(a -> a.getStatus() != ReservationStatus.PENDING && a.getStatus() != ReservationStatus.ACCEPTED)
                .map(tempReservation ->
                        reservationBuilder.buildReservationView()
                                .mapToClass(tempReservation)
                                .build())
                .toList();
    }

    @Override
    public List<ReservationViewDTO> getHistoryForStaff() {
        return reservationRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getReservationAt().compareTo(a.getReservationAt()))
                .filter(a -> a.getStatus() != ReservationStatus.PENDING && a.getStatus() != ReservationStatus.ACCEPTED)
                .map(reservation ->
                    reservationBuilder.buildReservationView()
                            .mapToClass(reservation)
                            .withUserUserName(reservation.getUser().getEmail())
                            .build()
                )
                .toList();
    }
}
