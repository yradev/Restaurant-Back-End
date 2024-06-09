package source.restaurant_web_project.models.builders.ConfigurationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.configurations.enums.DayOfWeeks;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationViewDTO;

import java.time.LocalTime;

@Component
public class ConfigurationViewClassBuilder extends ClassBuilderPattern<RestaurantConfigurationViewDTO> {
    protected ConfigurationViewClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new RestaurantConfigurationViewDTO());
    }

    @Override
    public ConfigurationViewClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ConfigurationViewClassBuilder cloneObject(RestaurantConfigurationViewDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    public ConfigurationViewClassBuilder withName(String name) {
        super.entity.setName(name);
        return this;
    }

    public ConfigurationViewClassBuilder withSubname(String subname) {
        super.entity.setSubname(subname);
        return this;
    }

    public ConfigurationViewClassBuilder withOpenTime(LocalTime openTime) {
        super.entity.setOpenTime(openTime);
        return this;
    }

    public ConfigurationViewClassBuilder withCloseTime(LocalTime closeTime) {
        super.entity.setCloseTime(closeTime);
        return this;
    }

    public ConfigurationViewClassBuilder withOpenDay(DayOfWeeks openDay) {
        super.entity.setOpenDay(openDay);
        return this;
    }

    public ConfigurationViewClassBuilder withCloseDay(DayOfWeeks closeDay) {
        super.entity.setCloseDay(closeDay);
        return this;
    }

    public ConfigurationViewClassBuilder withFirm(String firm) {
        super.entity.setFirm(firm);
        return this;
    }

    public ConfigurationViewClassBuilder withPhoneNumber(String phoneNumber) {
        super.entity.setPhone(phoneNumber);
        return this;
    }

    public ConfigurationViewClassBuilder withLocation(String location) {
        super.entity.setLocation(location);
        return this;
    }

    public ConfigurationViewClassBuilder withFacebookLink(String facebookLink) {
        super.entity.setFacebookLink(facebookLink);
        return this;
    }
    public ConfigurationViewClassBuilder withInstagramLink(String instagramLink) {
        super.entity.setInstagramLink(instagramLink);
        return this;
    }
    public ConfigurationViewClassBuilder withTwitterLink(String twitterLink) {
        super.entity.setTwitterLink(twitterLink);
        return this;
    }
    public ConfigurationViewClassBuilder withEmail(String email) {
        super.entity.setEmail(email);
        return this;
    }
    public ConfigurationViewClassBuilder withStreet(String street) {
        super.entity.setStreet(street);
        return this;
    }
    public ConfigurationViewClassBuilder withCity(String city) {
        super.entity.setCity(city);
        return this;
    }

}
