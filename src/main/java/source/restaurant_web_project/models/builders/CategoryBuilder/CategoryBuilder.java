package source.restaurant_web_project.models.builders.CategoryBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryBuilder {

    private final ModelMapper modelMapper;

    public CategoryBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryAddClassBuilder buildCategory(){
        return new CategoryAddClassBuilder(modelMapper);
    }

    public CategoryViewBuilder buildViewCategory(){
        return new CategoryViewBuilder(modelMapper);
    }

}
