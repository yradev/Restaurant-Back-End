package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import source.restaurant_web_project.configurations.authentication.JwtTokenUtil;
import source.restaurant_web_project.models.builders.DeliveryBuilder.DeliveryBuilder;
import source.restaurant_web_project.models.builders.UserBuilder.UserBuilder;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.models.entity.Token;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.repositories.DeliveryRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private String authToken;

    private Delivery delivery;

    private DeliveryBuilder deliveryBuilder = new DeliveryBuilder(new ModelMapper());
    private UserBuilder userBuilder = new UserBuilder(new ModelMapper());

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User tempUser = new User();

        tempUser.setPassword(bCryptPasswordEncoder.encode("123"));
        tempUser.setEmail("test");
        tempUser.setEnabled(true);
        userRepository.saveAndFlush(tempUser);

        Delivery tempDelivery = new Delivery();
        tempDelivery.setReceiveTime(LocalDateTime.now());
        tempDelivery.setAddress("newAddress");
        tempDelivery.setStatus(DeliveryStatus.PENDING);
        tempDelivery.setDeliver(userRepository.findUserByEmail("test"));
        delivery = deliveryRepository.saveAndFlush(tempDelivery);
    }

    @AfterEach
    void tearDown() {
        if(delivery!=null){
            deliveryRepository.delete(deliveryRepository.findDeliveryById(delivery.getId()));
        }

        userRepository.delete(userRepository.findUserByEmail("test"));
    }

    @WithMockUser(roles = "USER", username = "test",password = "123")
    @Test
    void addDelivery() throws Exception {
        deliveryRepository.delete(deliveryRepository.findDeliveriesByDeliver_Email("test").stream().findFirst().get());

        LocalDateTime localDateTime = LocalDateTime.now();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/deliveries/add")
                        .content(String.format("""
                                {
                                    "address": "testAddress123",
                                    "receivedTime": "%s",
                                    "items": [
                                        {
                                            "categoryName": "testCategoryName",
                                            "itemName":"testItemName",
                                            "count": "2",
                                            "price": "2"
                                        }
                                    ]
                                }
                                """, localDateTime))
                        .contentType("application/json"))
                .andExpect(status().isCreated());

        deliveryRepository.delete(deliveryRepository.findDeliveriesByDeliver_Email("test").stream().findFirst().get());
        delivery=null;
    }

    @WithMockUser(roles = "USER", username = "test",password = "123")
    @Test
    void getActiveDeliveryForLoggedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/deliveries/active"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test",password = "123")
    @Test
    void changeStatusForAuthUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/deliveries/change-status/"+delivery.getId()+"/ACCEPTED"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void changeStatusStaff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/deliveries/staff/change-status/"+delivery.getId()+"/ACCEPTED"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test",password = "123")
    @Test
    void removeDelivery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/deliveries/remove/"+delivery.getId()))
                .andExpect(status().isOk());

        delivery=null;
    }

    @WithMockUser(roles = "USER", username = "test",password = "123")
    @Test
    void getHistoryForAuthUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/deliveries/history/user"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void getHistoryForStaff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/deliveries/history/staff"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void getActiveDeliveriesForStaff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/deliveries/staff/active"))
                .andExpect(status().isOk());
    }
}