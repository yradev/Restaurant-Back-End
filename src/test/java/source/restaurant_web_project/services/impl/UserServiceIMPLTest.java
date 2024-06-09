package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.builders.UserBuilder.UserBuilder;
import source.restaurant_web_project.models.dto.user.RoleDTO;
import source.restaurant_web_project.models.dto.user.UserChangePasswordDTO;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceIMPLTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private RoleRepository roleRepository;

    private UserServiceIMPL test;

    @BeforeEach
    void setUp() {
        UserBuilder userBuilder = new UserBuilder(new ModelMapper());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        test = new UserServiceIMPL(userRepository, modelMapper, bCryptPasswordEncoder, roleRepository, userBuilder);
    }

    @Test
    void getUserData() {
        when(userRepository.findUserByEmail(any())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> test.getUserData(""));

        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        test.getUserData("");
        verify(modelMapper, times(1)).map(any(), any());
    }

    @Test
    void changePassword(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        User user = new User();

        userChangePasswordDTO.setCurrentPassword("valid");
        user.setPassword(bCryptPasswordEncoder.encode("pass"));

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        Assertions.assertThrows(BadRequestException.class,()->test.changePassword(userChangePasswordDTO));

        userChangePasswordDTO.setCurrentPassword("pass");
        userChangePasswordDTO.setNewPassword("newPass");

        test.changePassword(userChangePasswordDTO);
        verify(userRepository,times(1)).saveAndFlush(any());

    }

    @Test
    void changeEmail(){
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Assertions.assertThrows(UnauthorizedException.class,()->test.changeEmail("email"));

        User user = new User();
        user.setEmail("email");

        when(userRepository.findUserByEmail(securityContext.getAuthentication().getName())).thenReturn(user);
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(new User());

        Assertions.assertThrows(ConflictException.class,()->test.changeEmail("email"));

        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(null);

        test.changeEmail("email");
        verify(userRepository,times(1)).saveAndFlush(any());

    }

    @Test
    void getActiveRoles() {
        Role role = new Role();
        role.setName("name");
        when(roleRepository.findAll()).thenReturn(List.of(role));

        Assertions.assertEquals(test.getActiveRoles().get(0).getName(), "name");
    }

    @Test
    void changeUserCoreData() {
        UserControlDTO userControlDTO = new UserControlDTO();

        when(userRepository.findUserByEmail("email")).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class,()->test.changeUserCoreData("email",userControlDTO));

        User user = new User();

        user.setRoles(new ArrayList<>());

        when(userRepository.findUserByEmail("email")).thenReturn(user);

        RoleDTO role = new RoleDTO();
        role.setName("ROLE_USER");

        userControlDTO.setRoles(List.of(role));

        when(roleRepository.findByName(any())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class,()->test.changeUserCoreData("email",userControlDTO));

        Role role1 = new Role();
        role1.setName("ROLE_USER");

        when(roleRepository.findByName(any())).thenReturn(role1);

        userControlDTO.setEnabled(true);
        user.setEnabled(false);

        test.changeUserCoreData("email",userControlDTO);
        verify(userRepository,times(1)).saveAndFlush(any());

        userControlDTO.setEnabled(false);
        user.setEnabled(true);

        test.changeUserCoreData("email",userControlDTO);
        verify(userRepository,times(2)).saveAndFlush(any());
    }

    @Test
    void getUserCoreSettings() {
        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class,()->test.getUserCoreSettings(any()));

        user.setEmail("email");
        user.setRoles(new ArrayList<>());
        Role role = new Role();
        role.setName("ROLE_USER");
        user.getRoles().add(role);
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        Assertions.assertEquals(test.getUserCoreSettings("email").getRoles().get(0).getName(),"ROLE_USER");

    }

    @Test
    void deleteCurrentUser () {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        test.deleteCurrentUser();
        verify(userRepository,times(1)).delete(any());
    }
}