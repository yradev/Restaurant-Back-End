package source.restaurant_web_project.models.builders.ReservationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationBuilder {
    private final ModelMapper modelMapper;

    public ReservationBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservationAddClassBuilder buildReservation(){
        return new ReservationAddClassBuilder(modelMapper);
    }

    public ReservationViewClassBuilder buildReservationView(){
        return new ReservationViewClassBuilder(modelMapper);
    }
}
