package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantConfigurationRepository restaurantConfigurationRepository;

    @Test
    void getRestaurantInformation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/core"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void editRestaurantInformation() throws Exception {
     RestaurantConfigurationEntity core = restaurantConfigurationRepository.findById(1);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/core")
                        .content(String.format("""
                                {
                                "name":"%s",
                                "subname":"%s",
                                "openTime":"%s",
                                "closeTime":"%s",
                                "openDay":"%s",
                                "closeDay":"%s",
                                "phone","%s",
                                "location","%s",
                                "forUsContent","%s"
                                }
                                """,
                                core.getName(),
                                core.getSubname(),
                                core.getOpenTime()!=null?core.getOpenTime().toString():null,
                                core.getCloseTime()!=null?core.getCloseTime().toString():null,
                                core.getOpenDay()!=null?core.getOpenDay().name():null,
                                core.getCloseDay()!=null?core.getCloseDay().name():null,
                                core.getPhone(),
                                core.getLocation(),
                                core.getAboutUsContent()
                                ))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}