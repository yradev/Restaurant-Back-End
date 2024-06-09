package source.restaurant_web_project.models.builders.UserBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.*;

import java.util.List;

@Component
public class UserAddClassBuilder extends ClassBuilderPattern<User> {

    public UserAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new User());
    }


    @Override
    public UserAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public UserAddClassBuilder cloneObject(User entity) {
        super.cloneObject(entity);
        return this;
    }

    public UserAddClassBuilder withId(long id) {
        super.entity.setId(id);
        return this;
    }

    public UserAddClassBuilder withPassword(String password) {
        super.entity.setPassword(password);
        return this;
    }

    public UserAddClassBuilder withEmail(String email) {
        super.entity.setEmail(email);
        return this;
    }

    public UserAddClassBuilder withRoles(List<Role> roles) {
        super.entity.setRoles(roles);
        return this;
    }


    public UserAddClassBuilder withDeliveries(List<Delivery> deliveries) {
        super.entity.setDeliveries(deliveries);
        return this;
    }

    public UserAddClassBuilder withReservations(List<Reservation> reservations) {
        super.entity.setReservations(reservations);
        return this;
    }

    public UserAddClassBuilder isEnabled(boolean status) {
        super.entity.setEnabled(status);
        return this;
    }

}
