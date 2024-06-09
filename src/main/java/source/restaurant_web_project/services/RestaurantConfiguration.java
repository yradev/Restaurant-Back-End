package source.restaurant_web_project.services;

import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationEditDTO;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationViewDTO;

import java.io.IOException;
import java.sql.SQLException;

public interface RestaurantConfiguration {
    RestaurantConfigurationViewDTO getData() throws SQLException, IOException;
    void edit(MultipartFile imageForContacts,MultipartFile imageAboutUs, RestaurantConfigurationEditDTO restaurantConfigurationEditDTO) throws IOException, SQLException;
}
