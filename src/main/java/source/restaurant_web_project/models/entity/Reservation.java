package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {
    private LocalDateTime reservationAt;
    private LocalDateTime reservationFor;
    private int countOfGuests;
    private ReservationStatus status;
    private String phoneNumber;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public Reservation(){}

    public LocalDateTime getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(LocalDateTime reservationFor) {
        this.reservationFor = reservationFor;
    }

    public int getCountOfGuests() {
        return countOfGuests;
    }

    public void setCountOfGuests(int countOfPersons) {
        this.countOfGuests = countOfPersons;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getReservationAt() {
        return reservationAt;
    }

    public void setReservationAt(LocalDateTime reservationAt) {
        this.reservationAt = reservationAt;
    }
}
