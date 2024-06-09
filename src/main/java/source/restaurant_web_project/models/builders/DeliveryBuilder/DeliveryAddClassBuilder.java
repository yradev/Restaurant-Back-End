package source.restaurant_web_project.models.builders.DeliveryBuilder;

import org.modelmapper.ModelMapper;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.models.entity.DeliveryItem;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DeliveryAddClassBuilder extends ClassBuilderPattern<Delivery> {

    protected DeliveryAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new Delivery());
    }

    @Override
    public DeliveryAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public DeliveryAddClassBuilder cloneObject(Delivery entity) {
        super.cloneObject(entity);
        return this;
    }

    public DeliveryAddClassBuilder withId (long id){
        super.entity.setId(id);
        return this;
    }

    public DeliveryAddClassBuilder withReceiveTime(LocalDateTime receiveTime){
        super.entity.setReceiveTime(receiveTime);
        return this;
    }

    public DeliveryAddClassBuilder withStatus (DeliveryStatus deliveryStatus){
        super.entity.setStatus(deliveryStatus);
        return this;
    }

    public DeliveryAddClassBuilder withTotalPrice (BigDecimal totalPrice){
        super.entity.setTotalPrice(totalPrice);
        return this;
    }

    public DeliveryAddClassBuilder withAddress (String address){
        super.entity.setAddress(address);
        return this;
    }

    public DeliveryAddClassBuilder withPhoneNumber(String phoneNumber){
        super.entity.setPhoneNumber(phoneNumber);
        return this;
    }

    public DeliveryAddClassBuilder withDeliver(User user){
        super.entity.setDeliver(user);
        return this;
    }

    public DeliveryAddClassBuilder withItems (List<DeliveryItem> items){
        super.entity.setItems(items);
        return this;
    }
}

