package source.restaurant_web_project.models.builders.ConfigurationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.configurations.enums.DayOfWeeks;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;

import java.sql.Blob;
import java.time.LocalTime;

@Component
public class ConfigurationAddClassBuilder extends ClassBuilderPattern<RestaurantConfigurationEntity> {
    protected ConfigurationAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new RestaurantConfigurationEntity());
    }

    @Override
    public ConfigurationAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ConfigurationAddClassBuilder cloneObject(RestaurantConfigurationEntity entity) {
        super.cloneObject(entity);
        return this;
    }

    public ConfigurationAddClassBuilder withName(String name) {
        super.entity.setName(name);
        return this;
    }

    public ConfigurationAddClassBuilder withSubname(String subname) {
        super.entity.setSubname(subname);
        return this;
    }

    public ConfigurationAddClassBuilder withOpenTime(LocalTime openTime) {
        super.entity.setOpenTime(openTime);
        return this;
    }

    public ConfigurationAddClassBuilder withCloseTime(LocalTime closeTime) {
        super.entity.setCloseTime(closeTime);
        return this;
    }

    public ConfigurationAddClassBuilder withOpenDay(DayOfWeeks openDay) {
        super.entity.setOpenDay(openDay);
        return this;
    }

    public ConfigurationAddClassBuilder withCloseDay(DayOfWeeks closeDay) {
        super.entity.setCloseDay(closeDay);
        return this;
    }

    public ConfigurationAddClassBuilder withFirm(String firm) {
        super.entity.setFirm(firm);
        return this;
    }

    public ConfigurationAddClassBuilder withPhoneNumber(String phoneNumber) {
        super.entity.setPhone(phoneNumber);
        return this;
    }

    public ConfigurationAddClassBuilder withLocation(String location) {
        super.entity.setLocation(location);
        return this;
    }
    public ConfigurationAddClassBuilder withFacebookLink(String facebookLink) {
        super.entity.setFacebookLink(facebookLink);
        return this;
    }
    public ConfigurationAddClassBuilder withInstagramLink(String instagramLink) {
        super.entity.setInstagramLink(instagramLink);
        return this;
    }
    public ConfigurationAddClassBuilder withTwitterLink(String twitterLink) {
        super.entity.setTwitterLink(twitterLink);
        return this;
    }
    public ConfigurationAddClassBuilder withEmail(String email) {
        super.entity.setEmail(email);
        return this;
    }
    public ConfigurationAddClassBuilder withStreet(String street) {
        super.entity.setStreet(street);
        return this;
    }
    public ConfigurationAddClassBuilder withCity(String city) {
        super.entity.setCity(city);
        return this;
    }
}
