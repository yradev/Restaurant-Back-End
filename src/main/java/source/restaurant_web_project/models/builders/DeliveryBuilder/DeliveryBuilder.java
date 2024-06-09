package source.restaurant_web_project.models.builders.DeliveryBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DeliveryBuilder {

    private final ModelMapper modelMapper;

    public DeliveryBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DeliveryAddClassBuilder buildDelivery(){
        return new DeliveryAddClassBuilder(modelMapper);
    }

    public DeliveryViewClassBuilder buildDeliveryView(){
        return new DeliveryViewClassBuilder(modelMapper);
    }
}
