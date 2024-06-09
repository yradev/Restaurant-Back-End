package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.models.entity.Token;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.TokenRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private Token token;

    private User user;

    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User tempUser = new User();

        tempUser.setPassword(bCryptPasswordEncoder.encode("123"));
        tempUser.setEmail("test");
        tempUser.setEnabled(true);
        user = userRepository.saveAndFlush(tempUser);

        Token tempToken = new Token();
        tempToken.setEmail("test");
        tempToken.setToken("validToken");
        tempToken.setExpiryDate(LocalDateTime.now().plusHours(2));
        token = tokenRepository.saveAndFlush(tempToken);
    }

    @AfterEach
    void tearDown() {
        if (user != null) {
            userRepository.delete(userRepository.findById(user.getId()).get());
        }

        Token currToken = tokenRepository.findPasswordResetTokenByEmail("test");
        if (currToken != null) {
            tokenRepository.delete(currToken);
        }
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content("""
                                {
                                            "email":"test",
                                            "password":"123"
                                 }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .content("""
                                {
                                            "email":"test2@testemail.testek2kewke",
                                            "password":"123456"
                                 }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        userRepository.delete(userRepository.findUserByEmail("test2@testemail.testek2kewke"));
    }

    @Test
    void sendVerification() throws Exception {
        tokenRepository.delete(tokenRepository.findPasswordResetTokenByEmail("test"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/reset-password/verification/send")
                        .content("""
                                {
                                            "email":"test",
                                            "url":"http://test.url/"
                                 }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        tokenRepository.delete(tokenRepository.findPasswordResetTokenByEmail("test"));
    }

    @Test
    void verifyToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/auth/reset-password/verify/test/validToken"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/auth/reset-password/test")
                        .content("""
                                {
                                            "password":"1234567",
                                            "token":"validToken"
                                 }
                                """)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}