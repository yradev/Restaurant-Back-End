package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.services.ReservationService;

import java.net.URI;

@RestController
@RequestMapping("reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("active")
    public ResponseEntity<?>getActiveReservation(){
        return ResponseEntity.ok(reservationService.getActiveReservation());
    }

    @GetMapping("history/user")
    public ResponseEntity<?>getHistoryForUser(){
      return ResponseEntity.ok(reservationService.getHistoryForUser());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("history/staff")
    public ResponseEntity<?>getHistoryForStaff(){
        return ResponseEntity.ok(reservationService.getHistoryForStaff());
    }

    @PostMapping("add")
    public ResponseEntity<?>addReservation(@RequestBody NewReservationDTO newReservationDTO){
        reservationService.reserve(newReservationDTO);
        return ResponseEntity
                .created(URI.create(""))
                .build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?>cancelReservation(@PathVariable long id){
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }



    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("staff/active")
    public ResponseEntity<?>getReservationsForStaff(){
        return ResponseEntity.ok(reservationService.getStaffActiveReservations());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("status/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable long id, @PathVariable String status){
        reservationService.changeReservationStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
