package source.restaurant_web_project.models.builders.ItemBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.item.ItemViewDTO;

import java.math.BigDecimal;

@Component
public class ItemViewClassBuilder extends ClassBuilderPattern<ItemViewDTO> {
    protected ItemViewClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new ItemViewDTO());
    }

    @Override
    public ItemViewClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ItemViewClassBuilder cloneObject(ItemViewDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    public ItemViewClassBuilder withName (String name){
        super.entity.setName(name);
        return this;
    }

    public ItemViewClassBuilder withDescription (String description){
        super.entity.setDescription(description);
        return this;
    }

    public ItemViewClassBuilder withPrice (BigDecimal price){
        super.entity.setPrice(price);
        return this;
    }

    public ItemViewClassBuilder withImageUrl (String imageUrl){
        super.entity.setImageUrl(imageUrl);
        return this;
    }
}
