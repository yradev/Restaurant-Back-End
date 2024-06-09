package source.restaurant_web_project.configurations.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import source.restaurant_web_project.services.impl.AuthServiceIMPL;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfiguration {
    private final JwtTokenFilter jwtTokenFilter;

    public AuthConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, AuthServiceIMPL userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/auth/**").anonymous()
                .requestMatchers("/test/**").anonymous()
                .requestMatchers("/categories/**").permitAll()
                .requestMatchers("/core/**").permitAll()
                .requestMatchers("/items/**").permitAll()
                .requestMatchers("/lunch-menu/**").permitAll()
                .requestMatchers("/news/**").permitAll()
                .requestMatchers("/health").permitAll()
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
