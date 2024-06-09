package source.restaurant_web_project.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import source.restaurant_web_project.intercaptors.LocaleInterceptor;
import source.restaurant_web_project.intercaptors.NewDeliveryInterceptor;
import source.restaurant_web_project.intercaptors.NewReservationInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final NewDeliveryInterceptor newDeliveryInterceptor;
    private final NewReservationInterceptor newReservationInterceptor;
    private final LocaleInterceptor localeInterceptor;

    public InterceptorConfiguration(NewDeliveryInterceptor newDeliveryInterceptor, NewReservationInterceptor newReservationInterceptor, LocaleInterceptor localeInterceptor) {
        this.newDeliveryInterceptor = newDeliveryInterceptor;
        this.newReservationInterceptor = newReservationInterceptor;
        this.localeInterceptor = localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(newDeliveryInterceptor).addPathPatterns("/deliveries/add/**");
         registry.addInterceptor(newReservationInterceptor).addPathPatterns("/reservations/add/**");
         registry.addInterceptor(localeInterceptor).addPathPatterns("/**");
    }
}
