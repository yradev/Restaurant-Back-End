package source.restaurant_web_project.models.dto.reservation;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class NewReservationDTO {
    @NotEmpty
    @FutureOrPresent(message = "Reservation cant be in past!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationFor;
    private int countOfGuests;
    private String phoneNumber;

    public NewReservationDTO(){}

    public LocalDateTime getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(LocalDateTime reservationFor) {
        this.reservationFor = reservationFor;
    }

    public int getCountOfGuests() {
        return countOfGuests;
    }

    public void setCountOfGuests(int countOfGuests) {
        this.countOfGuests = countOfGuests;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
