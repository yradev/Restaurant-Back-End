package source.restaurant_web_project.models.builders.DeliveryBuilder;

import org.modelmapper.ModelMapper;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.delivery.DeliveryItemViewDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.util.List;

public class DeliveryViewClassBuilder extends ClassBuilderPattern<DeliveryViewDTO> {
    protected DeliveryViewClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new DeliveryViewDTO());
    }

    @Override
    public DeliveryViewClassBuilder cloneObject(DeliveryViewDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    @Override
    public DeliveryViewClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    public DeliveryViewClassBuilder withId(long id){
        super.entity.setId(id);
        return this;
    }

    public DeliveryViewClassBuilder withReceiveTime(String receiveTime){
        super.entity.setReceiveTime(receiveTime);
        return this;
    }

    public DeliveryViewClassBuilder withStatus(DeliveryStatus status){
        super.entity.setStatus(status);
        return this;
    }

    public DeliveryViewClassBuilder withTotalPrice(BigDecimal totalPrice){
        super.entity.setTotalPrice(totalPrice);
        return this;
    }

    public DeliveryViewClassBuilder withAddress(String address){
        super.entity.setAddress(address);
        return this;
    }

    public DeliveryViewClassBuilder withPhoneNumber(String phoneNumber){
        super.entity.setPhoneNumber(phoneNumber);
        return this;
    }

    public DeliveryViewClassBuilder withItems(List<DeliveryItemViewDTO> items){
        super.entity.setItems(items);
        return this;
    }

    public DeliveryViewClassBuilder withUsername(String email){
        super.entity.setUsername(email);
        return this;
    }
}

