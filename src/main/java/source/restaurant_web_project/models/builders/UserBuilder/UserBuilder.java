package source.restaurant_web_project.models.builders.UserBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserBuilder{
   private final ModelMapper modelMapper;


    public UserBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserAddClassBuilder buildUser(){
        return new UserAddClassBuilder(modelMapper);
    }

    public UserViewClassBuilder buildUserView(){
        return new UserViewClassBuilder(modelMapper);
    }
}
