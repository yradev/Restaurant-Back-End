package source.restaurant_web_project.models.dto.reservation;

import source.restaurant_web_project.models.entity.enums.ReservationStatus;

public class ReservationViewDTO {
    private long id;
   private String reservationAt;

    private String reservationFor;
    private int countOfGuests;
    private ReservationStatus status;
    private String userUsername;
    private String phoneNumber;

    public ReservationViewDTO(){}

    public String getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(String reservationFor) {

        this.reservationFor = reservationFor;
    }

    public int getCountOfGuests() {
        return countOfGuests;
    }

    public void setCountOfGuests(int countOfGuests) {
        this.countOfGuests = countOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReservationAt() {
        return reservationAt;
    }

    public void setReservationAt(String reservationAt) {
        this.reservationAt = reservationAt;
    }
}

