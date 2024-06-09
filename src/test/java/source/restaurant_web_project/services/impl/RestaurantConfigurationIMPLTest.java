package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.ConfigurationBuilder.ConfigurationBuilder;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationEditDTO;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantConfigurationIMPLTest {

    @Mock
    private RestaurantConfigurationRepository restaurantConfigurationRepository;

    @Spy
    private ModelMapper modelMapper;

    private RestaurantConfigurationIMPL testing;

    @Mock
    private LanguageTranslator languageTranslator;

    @Mock
    private AmazonS3Cloud fileUploaderToS3;

    @BeforeEach
    void SetUp() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(new ModelMapper());
        testing = new RestaurantConfigurationIMPL(restaurantConfigurationRepository, modelMapper, languageTranslator, configurationBuilder, fileUploaderToS3);
    }

    @Test
    void getData() throws SQLException, IOException {

        RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();
        restaurantConfigurationEntity.setName("name");
        restaurantConfigurationEntity.setSubname("name");
        restaurantConfigurationEntity.setAboutUsContent("name");
        restaurantConfigurationEntity.setLocation("name");
        when(restaurantConfigurationRepository.findById(1)).thenReturn(restaurantConfigurationEntity);
        when(languageTranslator.translateFromJson(any())).thenReturn("name");

        Assertions.assertEquals(testing.getData().getName(), "name");
    }

    @Test
    void edit() throws SQLException, IOException {
        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);


        RestaurantConfigurationEditDTO restaurantConfigurationEditDTO = new RestaurantConfigurationEditDTO();

        restaurantConfigurationEditDTO.setName("name");
        restaurantConfigurationEditDTO.setSubname("name");
        restaurantConfigurationEditDTO.setAboutUsContent("name");
        restaurantConfigurationEditDTO.setLocation("name");

        MultipartFile multipartFile = new MockMultipartFile("name", new byte[2]);

        RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();

        restaurantConfigurationEntity.setName("name");
        restaurantConfigurationEntity.setSubname("name");
        restaurantConfigurationEntity.setAboutUsContent("name");
        restaurantConfigurationEntity.setLocation("name");

        when(restaurantConfigurationRepository.findById(1)).thenReturn(restaurantConfigurationEntity);
        when(languageTranslator.translateToJSON(any(), any())).thenReturn("name");

        testing.edit(multipartFile, multipartFile, restaurantConfigurationEditDTO);
        verify(restaurantConfigurationRepository, times(1)).saveAndFlush(any());
    }
}