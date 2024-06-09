package source.restaurant_web_project.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.repositories.DeliveryRepository;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.TokenRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ScheduleTasks {
    private final TokenRepository tokenRepository;

    public ScheduleTasks(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanTokens(){
        tokenRepository.deleteAll(tokenRepository.findAll().stream()
                .filter(a->a.getExpiryDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList()));
    }

}
