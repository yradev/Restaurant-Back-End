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
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Reservation reservation;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User tempUser = new User();
        tempUser.setEmail("test");
        tempUser.setEnabled(true);
        tempUser.setPassword(new BCryptPasswordEncoder().encode("123"));
        User user = userRepository.saveAndFlush(tempUser);

        Reservation tempReservation = new Reservation();
        tempReservation.setReservationAt(LocalDateTime.now());
        tempReservation.setReservationFor(LocalDateTime.now().plusMinutes(50));
        tempReservation.setUser(user);
        tempReservation.setStatus(ReservationStatus.PENDING);
        tempReservation.setCountOfGuests(1);
        tempReservation.setPhoneNumber("089529394929");

        reservation = reservationRepository.saveAndFlush(tempReservation);
    }

    @AfterEach
    void tearDown() {
        if (reservation != null) {
            reservationRepository.delete(reservationRepository.findById(reservation.getId()).get());

        }
        userRepository.delete(userRepository.findUserByEmail("test"));
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void getActiveReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reservations/active"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void getHistoryForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reservations/history/user"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void getHistoryForStaff() throws Exception {
        reservation.setStatus(ReservationStatus.HISTORY);
        reservationRepository.saveAndFlush(reservation);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reservations/history/staff"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void addReservation() throws Exception {
        reservationRepository.delete(reservationRepository.findReservationById(reservation.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/reservations/add")
                        .content(String.format("""
                                {
                                "reservationFor":"%s",
                                "countOfPersons":"2",
                                "phoneNumber":"-282392949493"
                                }
                                """,LocalDateTime.now().plusMinutes(10)))
                        .contentType("application/json"))
                .andExpect(status().isCreated());

       reservationRepository.delete(reservationRepository.findReservationsByPhoneNumber("-282392949493").get(0));
        reservation = null;
    }

    @WithMockUser(roles = "USER", username = "test", password = "123")
    @Test
    void cancelReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/reservations/delete/"+ reservation.getId()))
                .andExpect(status().isOk());

        reservation = null;
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void getReservationsForStaff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/reservations/staff/active"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "STAFF")
    @Test
    void changeRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/reservations/status/"+reservation.getId() + "/ACCEPTED"))
                .andExpect(status().isOk());
    }
}