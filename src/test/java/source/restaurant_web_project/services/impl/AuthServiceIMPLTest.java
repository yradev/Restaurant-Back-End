package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.builders.TokenClassBuilder;
import source.restaurant_web_project.models.builders.UserBuilder.UserBuilder;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.Token;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.TokenRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.util.EmailSender;
import source.restaurant_web_project.util.LanguageTranslator;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceIMPLTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RestaurantConfigurationRepository confgurationRepository;

    @Mock
    private EmailSender emailSender;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Spy
    private ModelMapper modelMapper;
    private AuthServiceIMPL testing;
    @Mock
    private LanguageTranslator languageTranslator;

    @BeforeEach
    void SetUp() {
        UserBuilder userBuilder = new UserBuilder(new ModelMapper());
        TokenClassBuilder tokenBuilder = new TokenClassBuilder(new ModelMapper());
        testing = new AuthServiceIMPL(tokenRepository, userRepository, emailSender, bCryptPasswordEncoder, languageTranslator, modelMapper, roleRepository, confgurationRepository, userBuilder, tokenBuilder);
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findUserByEmail("invalidEmail")).thenReturn(null);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testing.loadUserByUsername("invalidEmail"));

        User user = new User();
        user.setEnabled(false);
        when(userRepository.findUserByEmail("validEmail")).thenReturn(user);

        Assertions.assertThrows(
                UnauthorizedException.class, () -> testing.loadUserByUsername("validEmail"));

        user.setEnabled(true);
        user.setEmail("test@test.test");
        user.setPassword("password");
        Role role = new Role();
        role.setName("name");

        user.setRoles(List.of(role));

        Assertions.assertEquals(testing.loadUserByUsername("validEmail").getUsername(), "test@test.test");
    }

    @Test
    void testLogin() {
        String token = "asd";
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter("firstName", "Spring");
        mockHttpServletRequest.setParameter("lastName", "Test");

        Role role = new Role();
        role.setName("testRole");

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "test", "pass", List.of(new SimpleGrantedAuthority(role.getName()))
        );

        testing.login(mockHttpServletRequest, user);

        Assertions.assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    void testRegister() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();

        User user = new User();
        user.setEmail("email");
        user.setPassword("password");

        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Assertions.assertThrows(ConflictException.class, () -> testing.register(userRegisterDTO));

        when(userRepository.findUserByEmail(any())).thenReturn(null);
        Role role = new Role();
        role.setName("ROLE_USER");

        testing.register(userRegisterDTO);
        verify(userRepository, times(1)).save(any());
 }

    @Test
    void testSendVerifyMessage() {
        Assertions.assertThrows(NotFoundException.class, () -> testing.sendVerifyMessage("", ""));

        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().minusMinutes(10));


        User user = new User();
        user.setEmail("email");

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        token.setExpiryDate(LocalDateTime.now().plusMinutes(2));

        when(tokenRepository.findPasswordResetTokenByEmail(any())).thenReturn(token);

        Assertions.assertThrows(ConflictException.class, () -> testing.sendVerifyMessage("", ""));

        token.setExpiryDate(LocalDateTime.now().minusMinutes(2));

        RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();
        restaurantConfigurationEntity.setName("name");

        when(confgurationRepository.findAll()).thenReturn(List.of(restaurantConfigurationEntity));
        testing.sendVerifyMessage("", "");
        verify(emailSender, times(1)).sendEmail(any(), any(), any());
    }

    @Test
    void testResetPassword() {

        when(tokenRepository.findPasswordResetTokenByEmailAndToken(any(),any())).thenReturn(null);

        Assertions.assertThrows(BadRequestException.class, () -> testing.resetPassword("","",""));

        Token token = new Token();
        when(tokenRepository.findPasswordResetTokenByEmailAndToken(any(),any())).thenReturn(token);

        token.setExpiryDate(LocalDateTime.now().minusMinutes(2));

        Assertions.assertThrows(BadRequestException.class, () -> testing.resetPassword("","",""));

        token.setExpiryDate(LocalDateTime.now().plusMinutes(2));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        when(bCryptPasswordEncoder.encode(any())).thenReturn(encoder.encode("password"));


        when(userRepository.findUserByEmail(any())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> testing.resetPassword("","",""));

        User user = new User();

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        testing.resetPassword("","","");
        verify(userRepository,times(1)).save(any());
        verify(tokenRepository,times(2)).delete(any());
    }

    @Test
    void testVerifyToken() {
        when(tokenRepository.findPasswordResetTokenByEmailAndToken(any(),any())).thenReturn(null);
        Assertions.assertFalse(testing.verifyToken("",""));

        Token token = new Token();
        token.setExpiryDate(LocalDateTime.now().minusMinutes(5));

        when(tokenRepository.findPasswordResetTokenByEmailAndToken(any(),any())).thenReturn(token);

        Assertions.assertFalse(testing.verifyToken("",""));

        token.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        Assertions.assertTrue(testing.verifyToken("",""));

    }
}