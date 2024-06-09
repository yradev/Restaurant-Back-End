package source.restaurant_web_project.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.models.builders.TokenClassBuilder;
import source.restaurant_web_project.models.builders.UserBuilder.UserBuilder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.models.entity.Token;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.repositories.RoleRepository;
import source.restaurant_web_project.repositories.TokenRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.AuthService;
import source.restaurant_web_project.util.EmailSender;
import source.restaurant_web_project.util.LanguageTranslator;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceIMPL implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LanguageTranslator languageTranslator;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final RestaurantConfigurationRepository confgurationRepository;

    private final UserBuilder userBuilder;

    private final TokenClassBuilder tokenBuilder;

    @Autowired
    public AuthServiceIMPL(TokenRepository passwordResetTokenRepository, UserRepository userRepository, EmailSender emailSender, BCryptPasswordEncoder bCryptPasswordEncoder, LanguageTranslator languageTranslator, ModelMapper modelMapper, RoleRepository roleRepository, RestaurantConfigurationRepository confgurationRepository, UserBuilder userBuilder, TokenClassBuilder tokenBuilder) {
        this.tokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.languageTranslator = languageTranslator;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.confgurationRepository = confgurationRepository;
        this.userBuilder = userBuilder;
        this.tokenBuilder = tokenBuilder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new NotFoundException("Username not found!");
        }

        if (!user.isEnabled()) {
            throw new UnauthorizedException("User is disabled!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }

    @Override
    public void login(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findUserByEmail(userRegisterDTO.getEmail()) != null) {
            throw new ConflictException("We have user with this email!");
        }

        List<Role> roles = new ArrayList<>();

        if (userRepository.count() == 0) {
            roles.addAll(roleRepository.findAll());
        } else {
            Role role = roleRepository.findByName("ROLE_USER");
            roles.add(role);
        }

        User user = userBuilder.buildUser()
                .mapToClass(userRegisterDTO)
                .withPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()))
                .isEnabled(true)
                .withRoles(roles)
                .build();

        userRepository.save(user);
    }

    @Override
    public void sendVerifyMessage(String email, String url) {
        if (userRepository.findUserByEmail(email) == null) {
            throw new NotFoundException(String.format("We dont have user with email %s!", email));
        }

        Token passwordChangeToken = tokenRepository.findPasswordResetTokenByEmail(email);

        if (passwordChangeToken != null) {
            if (LocalDateTime.now().isAfter(passwordChangeToken.getExpiryDate())) {
                tokenRepository.delete(passwordChangeToken);
            } else {
                throw new ConflictException("We have active token for this email!");
            }
        }

        passwordChangeToken = tokenBuilder
                .withToken(UUID.randomUUID().toString())
                .withEmail(email)
                .withExpireDate(LocalDateTime.now().plusMinutes(10))
                .build();

        tokenRepository.save(passwordChangeToken);

        String urlVerify = String.format("%s?token=%s&email=%s", url, passwordChangeToken.getToken(), email);
        String restaurantName = languageTranslator.translateFromJson(confgurationRepository.findAll().stream().findFirst().get().getName());

        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        String pattern = resourceBundle.getString("resetPasswordBody");
        String message = MessageFormat.format(pattern, urlVerify, restaurantName);

        emailSender.sendEmail(email, "Password reset", message);
    }

    @Override
    public void resetPassword(String token, String email, String password) {
        if (!this.verifyToken(email, token)) {
            throw new BadRequestException("Invalid token");
        }

        User currentUser = userRepository.findUserByEmail(email);

        if (currentUser == null) {
            throw new NotFoundException(String.format("User with email %s doesnt exist!", email));
        }

       User user = userBuilder.buildUser()
                .cloneObject(currentUser)
                .withPassword(bCryptPasswordEncoder.encode(password))
                .build();

        userRepository.save(user);

        tokenRepository.delete(tokenRepository.findPasswordResetTokenByEmailAndToken(email, token));
    }

    @Override
    public boolean verifyToken(String email, String token) {
        Token currToken = tokenRepository.findPasswordResetTokenByEmailAndToken(email, token);

        if (currToken == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(currToken.getExpiryDate())) {
            tokenRepository.delete(currToken);
            return false;
        }

        return true;
    }
}