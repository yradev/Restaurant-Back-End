package source.restaurant_web_project.models.builders.ItemBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemBuilder {
    private final ModelMapper modelMapper;

    public ItemBuilder(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ItemAddClassBuilder buildItem(){
        return new ItemAddClassBuilder(modelMapper);
    }

    public ItemViewClassBuilder buildItemView(){
        return new ItemViewClassBuilder(modelMapper);
    }

    public ItemFavoritesAddClassBuilder buildFavorites(){
        return new ItemFavoritesAddClassBuilder(modelMapper);
    }
}
