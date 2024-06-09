package source.restaurant_web_project.models.builders.UserBuilder;

import org.modelmapper.ModelMapper;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.user.UserDataViewDTO;
import source.restaurant_web_project.models.entity.User;

public class UserViewClassBuilder extends ClassBuilderPattern<UserDataViewDTO> {

    protected UserViewClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new UserDataViewDTO());
    }

    protected UserViewClassBuilder(ModelMapper modelMapper, UserDataViewDTO entity) {
        super(modelMapper, entity);
    }

    @Override
    public ClassBuilderPattern<UserDataViewDTO> cloneObject(UserDataViewDTO entity) {
        return super.cloneObject(entity);
    }

    @Override
    public ClassBuilderPattern<UserDataViewDTO> mapToClass(Object object) {
        return super.mapToClass(object);
    }

    @Override
    public UserDataViewDTO build() {
        return super.build();
    }
}
