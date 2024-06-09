package source.restaurant_web_project.models.builders.ConfigurationBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationBuilder {
    private ModelMapper modelMapper;

    public ConfigurationBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ConfigurationAddClassBuilder buildConfig(){
        return new ConfigurationAddClassBuilder(modelMapper);
    }

    public ConfigurationViewClassBuilder buildConfigView(){
        return new ConfigurationViewClassBuilder(modelMapper);
    }
}
