package source.restaurant_web_project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationEditDTO;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationViewDTO;
import source.restaurant_web_project.services.RestaurantConfiguration;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("core")
public class RestaurantController {

    private final RestaurantConfiguration restaurantConfiguration;
    private final MessageSource messageSource;
    public RestaurantController(RestaurantConfiguration restaurantConfiguration, MessageSource messageSource) {
        this.restaurantConfiguration = restaurantConfiguration;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public ResponseEntity<RestaurantConfigurationViewDTO>getRestaurantInformation() throws SQLException, IOException {
        RestaurantConfigurationViewDTO restaurantConfigurationEditDTO = restaurantConfiguration.getData();
        return ResponseEntity.ok(restaurantConfigurationEditDTO);
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping()
    public ResponseEntity<RestaurantConfigurationEditDTO>editRestaurantInformation(@RequestParam(value = "imageForContacts",required = false) MultipartFile imageForContacts, @RequestParam(value = "imageAboutUs",required = false) MultipartFile imageAboutUs, @Valid RestaurantConfigurationEditDTO restaurantConfigurationEditDTO) throws SQLException, IOException {
        restaurantConfiguration.edit(imageForContacts,imageAboutUs, restaurantConfigurationEditDTO);
        return ResponseEntity.ok().build();
    }
}
