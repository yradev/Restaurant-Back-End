package source.restaurant_web_project.intercaptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.models.entity.DeliveryItem;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.services.DeliveryService;

import java.util.List;

@Component
public class NewDeliveryInterceptor implements HandlerInterceptor {
    private final DeliveryService deliveryService;

    public NewDeliveryInterceptor(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try{
            deliveryService.getActiveDelivery();
            throw new BadRequestException("We can have only 1 active delivery!");
        }catch (NotFoundException exception) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }
}
