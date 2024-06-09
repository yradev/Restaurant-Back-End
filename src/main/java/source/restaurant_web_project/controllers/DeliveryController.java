package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.services.DeliveryService;

import java.net.URI;
@RestController
@RequestMapping("deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("add")
    public ResponseEntity<?>addDelivery(@RequestBody AddDeliveryDTO addDeliveryDTO) {
        deliveryService.addNewDelivery(addDeliveryDTO);

        return ResponseEntity
                .created(URI.create("")).build();
    }

    @GetMapping("/active")
    public ResponseEntity<?>getActiveDeliveryForLoggedUser(){
        return ResponseEntity.ok(deliveryService.getActiveDelivery());
    }

    @PutMapping("/change-status/{id}/{status}")
    public ResponseEntity<?> changeStatusForAuthUser(@PathVariable long id, @PathVariable String status){
        deliveryService.changeStatusForAuthUser(id,status);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/staff/change-status/{id}/{status}")
    public ResponseEntity<?>changeStatusStaff(@PathVariable long id,@PathVariable String status){
        deliveryService.changeStatusForStaff(id,status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?>removeDelivery(@PathVariable long id){
        deliveryService.removeDelivery(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/user")
    public ResponseEntity<?>getHistoryForAuthUser(){
        return ResponseEntity.ok(deliveryService.getHistoryForAuthUser());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/history/staff")
    public ResponseEntity<?>getHistoryForStaff(){
        return ResponseEntity.ok(deliveryService.getHistoryForStaff());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/staff/active")
    public ResponseEntity<?>getActiveDeliveriesForStaff(){
        return ResponseEntity.ok(deliveryService.getActiveDeliveriesForStaff());
    }
}