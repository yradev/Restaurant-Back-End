package source.restaurant_web_project.models.builders.ItemBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.item.ItemFavoritesDTO;

import java.math.BigDecimal;

@Component
public class ItemFavoritesAddClassBuilder extends ClassBuilderPattern<ItemFavoritesDTO> {
    protected ItemFavoritesAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new ItemFavoritesDTO());
    }

    @Override
    public ItemFavoritesAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ItemFavoritesAddClassBuilder cloneObject(ItemFavoritesDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    public ItemFavoritesAddClassBuilder withItemName (String name){
        super.entity.setItemName(name);
        return this;
    }

    public ItemFavoritesAddClassBuilder withDescription (String description){
        super.entity.setDescription(description);
        return this;
    }

    public ItemFavoritesAddClassBuilder withPrice (BigDecimal price){
        super.entity.setPrice(price);
        return this;
    }

    public ItemFavoritesAddClassBuilder withImageUrl (String imageUrl){
        super.entity.setImageUrl(imageUrl);
        return this;
    }

    public ItemFavoritesAddClassBuilder withItemPosition (long itemPosition){
        super.entity.setItemPosition(itemPosition);
        return this;
    }

    public ItemFavoritesAddClassBuilder withCategoryPosition (long categoryPosition){
        super.entity.setCategoryPosition(categoryPosition);
        return this;
    }

    public ItemFavoritesAddClassBuilder withCategoryName (String categoryName){
        super.entity.setCategoryName(categoryName);
        return this;
    }
}
