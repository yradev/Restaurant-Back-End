package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.ConfigurationBuilder.ConfigurationBuilder;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationEditDTO;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationViewDTO;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.services.RestaurantConfiguration;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Service
public class RestaurantConfigurationIMPL implements RestaurantConfiguration {
    private final RestaurantConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;
    private final LanguageTranslator languageTranslator;
    private final ConfigurationBuilder configurationBuilder;

    private final AmazonS3Cloud cloud;

    public RestaurantConfigurationIMPL(RestaurantConfigurationRepository configurationRepository, ModelMapper modelMapper, LanguageTranslator languageTranslator, ConfigurationBuilder configurationBuilder, AmazonS3Cloud cloud) {
        this.configurationRepository = configurationRepository;
        this.modelMapper = modelMapper;
        this.languageTranslator = languageTranslator;
        this.configurationBuilder = configurationBuilder;
        this.cloud = cloud;
    }

    @Override
    public RestaurantConfigurationViewDTO getData() {
        RestaurantConfigurationEntity restaurantConfigurationEntity = configurationRepository.findById(1);

        return configurationBuilder.buildConfigView()
                .mapToClass(restaurantConfigurationEntity)
                .withLocation(languageTranslator.translateFromJson(restaurantConfigurationEntity.getLocation()))
                .withSubname(languageTranslator.translateFromJson(restaurantConfigurationEntity.getSubname()))
                .withName(languageTranslator.translateFromJson(restaurantConfigurationEntity.getName()))
                .withCity(languageTranslator.translateFromJson(restaurantConfigurationEntity.getCity()))
                .withStreet(languageTranslator.translateFromJson(restaurantConfigurationEntity.getStreet()))
                .build();
    }

    @Override
    public void edit(MultipartFile imageForContacts, MultipartFile imageAboutUs, RestaurantConfigurationEditDTO restaurantConfigurationEditDTO) {
        RestaurantConfigurationEntity configuration = configurationRepository.findById(1);

        String description = languageTranslator.translateToJSON(restaurantConfigurationEditDTO.getSubname(), configuration.getSubname());
        String location = languageTranslator.translateToJSON(restaurantConfigurationEditDTO.getLocation(), configuration.getLocation());
        String name = languageTranslator.translateToJSON(restaurantConfigurationEditDTO.getName(), configuration.getName());
        String city = languageTranslator.translateToJSON(restaurantConfigurationEditDTO.getCity(), configuration.getCity());
        String street = languageTranslator.translateToJSON(restaurantConfigurationEditDTO.getStreet(), configuration.getStreet());

        configurationBuilder.buildConfig()
                .cloneObject(configuration)
                .mapToClass(restaurantConfigurationEditDTO)
                .withName(name)
                .withSubname(description)
                .withLocation(location)
                .withCity(city)
                .withStreet(street)
                .build();

        configurationRepository.saveAndFlush(configuration);
    }
}
