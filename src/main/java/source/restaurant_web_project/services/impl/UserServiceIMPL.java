package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.models.builders.UserBuilder.UserBuilder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.dto.user.*;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserBuilder userBuilder;

    public UserServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserBuilder userBuilder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userBuilder = userBuilder;
    }


    @Override
    public UserDataViewDTO getUserData(String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException("We dont have user with this email!");
        }

        return modelMapper.map(user, UserDataViewDTO.class);
    }

    @Override
    public void changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        User currUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!bCryptPasswordEncoder.matches(userChangePasswordDTO.getCurrentPassword(), currUser.getPassword())) {
            throw new BadRequestException("Old password is invalid!");
        }

        User user = userBuilder.buildUser()
                .cloneObject(currUser)
                .withPassword(bCryptPasswordEncoder.encode(userChangePasswordDTO.getNewPassword()))
                .build();

        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeEmail(String email) {
        User currUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (currUser == null) {
            throw new UnauthorizedException();
        }

        if (userRepository.findUserByEmail(email) != null) {
            throw new ConflictException("We have user with this email!");
        }

        User user = userBuilder.buildUser()
                .cloneObject(currUser)
                .withEmail(email)
                .build();

        userRepository.saveAndFlush(user);
    }

    @Override
    public List<RoleDTO> getActiveRoles() {
        return roleRepository.findAll().stream().map(a -> modelMapper.map(a, RoleDTO.class)).toList();
    }

    @Override
    public void changeUserCoreData(String email, UserControlDTO userControlDTO) {
        User currUser = userRepository.findUserByEmail(email);

        if (currUser == null) {
            throw new NotFoundException("We dont have currUser with this email!");
        }

        List<Role> roles = new ArrayList<>();

        userControlDTO.getRoles().stream()
                .map(a -> {
                    Role role = roleRepository.findByName(a.getName());
                    if (role == null) {
                        throw new NotFoundException(String.format("We dont have role with name %s !", a.getName()));
                    }
                    return role;
                })
                .forEach(roles::add);

        roles.add(roleRepository.findByName("ROLE_USER"));

        User user = userBuilder.buildUser()
                .cloneObject(currUser)
                .withRoles(roles)
                .isEnabled(userControlDTO.isEnabled())
                .build();

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserControlDTO getUserCoreSettings(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException("We dont have user with this email!");
        }

        UserControlDTO userControlDTO = new UserControlDTO();
        modelMapper.map(user, userControlDTO);
        return userControlDTO;
    }

    @Override
    public void deleteCurrentUser() {
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        SecurityContextHolder.clearContext();
        userRepository.delete(user);
    }

    @Override
    public List<UserDataViewDTO> getUsersByEmail(String email) {
        if (email == null) {
            return userRepository.findAllByOrderByEmail().stream()
                    .map(a ->
                            userBuilder.buildUserView()
                                    .mapToClass(a)
                                    .build()
                    )
                    .collect(Collectors.toList());
        }
        return userRepository.findUsersByEmailContainingOrderByEmail(email).stream()
                .map(a ->
                        userBuilder.buildUserView()
                                .mapToClass(a)
                                .build()
                )
                .collect(Collectors.toList());
    }

}
