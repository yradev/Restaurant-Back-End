package source.restaurant_web_project.configurations.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.services.AuthService;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtUtil;
    private final AuthService authService;

    public JwtTokenFilter(JwtTokenUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authentication");

        if (header==null || !jwtUtil.validateAccessToken(header.trim())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.trim();

        try {
            UserDetails userDetails = authService.loadUserByUsername(jwtUtil.getSubject(token));
            authService.login(request, userDetails);

            filterChain.doFilter(request, response);
        }catch (NotFoundException exception){
            filterChain.doFilter(request,response);
        }
    }
}