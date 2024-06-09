package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.builders.ReservationBuilder.ReservationBuilder;
import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceIMPLTest {

    private ReservationServiceIMPL test;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        ReservationBuilder reservationBuilder = new ReservationBuilder(new ModelMapper());
        test = new ReservationServiceIMPL(reservationRepository,userRepository,modelMapper, reservationBuilder);
    }

    @Test
    void reserve() {
        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        when(reservationRepository.saveAndFlush(any())).thenReturn(reservation);
        test.reserve(new NewReservationDTO());
        verify(reservationRepository,times(1)).saveAndFlush(any());
    }

    @Test
    void getStaffReservations() {
        Reservation reservation = new Reservation();
        User user = new User();
        user.setEmail("email");
        reservation.setUser(user);
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        test.getStaffActiveReservations();
        verify(reservationRepository,times(1)).findAll();
    }

    @Test
    void changeReservationStatus() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(new Reservation()));

        test.changeReservationStatus(1L,"PENDING");
        verify(reservationRepository,times(1)).saveAndFlush(any());
    }

    @Test
    void getActiveReservation(){
        Assertions.assertThrows(NotFoundException.class,()-> test.getActiveReservation());

        this.changeReservationStatus();

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        reservation.setStatus(ReservationStatus.PENDING);

        reservation.setReservationFor(LocalDateTime.now().minusMinutes(2));
        when(reservationRepository.findReservationsByUser_Email(any())).thenReturn(List.of(reservation));

        assertNull(test.getActiveReservation());

        reservation.setReservationFor(LocalDateTime.now().plusMinutes(2));

        Assertions.assertEquals(test.getActiveReservation().getId(),1L);
    }

    @Test
    void cancelReservation(){
        User user = new User();

        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Reservation reservation = new Reservation();

        reservation.setUser(new User());
        when(reservationRepository.findReservationById(1L)).thenReturn(reservation);

        Assertions.assertThrows(BadRequestException.class,()->test.cancelReservation(1L));

        reservation.setUser(user);

        test.cancelReservation(1L);
        verify(reservationRepository,times(1)).delete(any());
    }

    @Test
    void getHistoryForUser(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Reservation reservation = new Reservation();

        reservation.setReservationAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.HISTORY);
        when(reservationRepository.findReservationsByUser_Email(any())).thenReturn(List.of(reservation));

        Assertions.assertEquals(test.getHistoryForUser().size(),1);
    }

    @Test
    void getHistoryForStaff(){
        Reservation reservation = new Reservation();
        reservation.setReservationAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.HISTORY);

        User user = new User();
        user.setEmail("email");
        reservation.setUser(user);

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        Assertions.assertEquals(test.getHistoryForStaff().size(),1);
    }
}