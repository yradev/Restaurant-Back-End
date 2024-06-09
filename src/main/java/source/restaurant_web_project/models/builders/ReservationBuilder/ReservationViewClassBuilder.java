package source.restaurant_web_project.models.builders.ReservationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;

@Component
public class ReservationViewClassBuilder extends ClassBuilderPattern<ReservationViewDTO> {
    protected ReservationViewClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new ReservationViewDTO());
    }

    @Override
    public ReservationViewClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ReservationViewClassBuilder cloneObject(ReservationViewDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    public ReservationViewClassBuilder withId(long id){
        super.entity.setId(id);
        return this;
    }

    public ReservationViewClassBuilder withReservationAt(String reservationAt){
        super.entity.setReservationAt(reservationAt);
        return this;
    }

    public ReservationViewClassBuilder withReservationFor(String reservationFor){
        super.entity.setReservationFor(reservationFor);
        return this;
    }

    public ReservationViewClassBuilder withCountOfGuests(int count){
        super.entity.setCountOfGuests(count);
        return this;
    }

    public ReservationViewClassBuilder withStatus(ReservationStatus status){
        super.entity.setStatus(status);
        return this;
    }

    public ReservationViewClassBuilder withPhoneNumber(String phoneNumber){
        super.entity.setPhoneNumber(phoneNumber);
        return this;
    }

    public ReservationViewClassBuilder withUserUserName(String user){
        super.entity.setUserUsername(user);
        return this;
    }
}
