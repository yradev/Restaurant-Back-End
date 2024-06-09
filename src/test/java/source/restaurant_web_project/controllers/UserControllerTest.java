package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        User tempUser = new User();
        tempUser.setEnabled(true);
        tempUser.setPassword(new BCryptPasswordEncoder().encode("123"));
        tempUser.setEmail("test");
        user = userRepository.saveAndFlush(tempUser);
    }

    @AfterEach
    void tearDown() {
        if (user != null) {
            userRepository.delete(userRepository.findUserByEmail("test"));
        }
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void getUserData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void changeUserEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/change/email/tes"))
                .andExpect(status().isOk());

        userRepository.delete(userRepository.findUserByEmail("tes"));
        user = null;
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void changeUserPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/change/password")
                        .content("""
                                {
                                "currentPassword":"123",
                                "newPassword":"123456"
                                }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void deleteCurrentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/delete"))
                .andExpect(status().isOk());

        user = null;
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void testGetUserData() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/"+ user.getEmail()))
                    .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void getActiveRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/roles"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void getUserCoreSetings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/control/"+user.getEmail()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void editUserCore() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/control/"+user.getEmail())
                        .content("""
                                {
                                "enabled":"true",
                                "roles":[
                                {
                                "name":"ROLE_USER"
                                }
                                ]
                                }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}