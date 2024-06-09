package source.restaurant_web_project.models.builders.ReservationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;

import java.time.LocalDateTime;

@Component
public class ReservationAddClassBuilder extends ClassBuilderPattern<Reservation> {
    protected ReservationAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new Reservation());
    }

    @Override
    public ReservationAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ReservationAddClassBuilder cloneObject(Reservation entity) {
        super.cloneObject(entity);
        return this;
    }

    public ReservationAddClassBuilder withReservationAt(LocalDateTime reservationAt){
        super.entity.setReservationAt(reservationAt);
        return this;
    }

    public ReservationAddClassBuilder withReservationFor(LocalDateTime reservationFor){
        super.entity.setReservationFor(reservationFor);
        return this;
    }

    public ReservationAddClassBuilder withCountOfGuests(int count){
        super.entity.setCountOfGuests(count);
        return this;
    }

    public ReservationAddClassBuilder withStatus(ReservationStatus status){
        super.entity.setStatus(status);
        return this;
    }

    public ReservationAddClassBuilder withPhoneNumber(String phoneNumber){
        super.entity.setPhoneNumber(phoneNumber);
        return this;
    }

    public ReservationAddClassBuilder withUser(User user){
        super.entity.setUser(user);
        return this;
    }
}
