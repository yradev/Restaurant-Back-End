package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;

import java.util.List;

public interface ReservationService {
    void reserve(NewReservationDTO newReservationDTO);

    ReservationViewDTO getActiveReservation();

    List<ReservationViewDTO> getStaffActiveReservations();

    void changeReservationStatus(long reservationId, String status);

    void cancelReservation(long id);

    List<ReservationViewDTO> getHistoryForUser();

    List<ReservationViewDTO> getHistoryForStaff();
}
