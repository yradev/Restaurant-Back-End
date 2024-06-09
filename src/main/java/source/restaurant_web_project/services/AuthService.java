package source.restaurant_web_project.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import source.restaurant_web_project.models.dto.authentication.UserRegisterDTO;

public interface AuthService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

    void register(UserRegisterDTO userRegisterDTO);

    void login(HttpServletRequest request, UserDetails userDetails);


    void sendVerifyMessage(String email, String url);

    void resetPassword(String token, String email, String password);

    boolean verifyToken(String email, String token);
}
