package source.restaurant_web_project.intercaptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.services.ReservationService;

import java.util.List;

@Component
public class NewReservationInterceptor implements HandlerInterceptor {
    private final ReservationService reservationService;

    public NewReservationInterceptor(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       try{
           ReservationViewDTO reservation = reservationService.getActiveReservation();

           if (reservation.getStatus() == ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.ACCEPTED) {
               throw new BadRequestException("You can have only one active reservation!");
           }
           return HandlerInterceptor.super.preHandle(request, response, handler);
       }catch (NotFoundException exc){
           return HandlerInterceptor.super.preHandle(request, response, handler);
       }
    }
}
