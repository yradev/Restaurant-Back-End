package source.restaurant_web_project.models.builders.ItemBuilder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;

import java.math.BigDecimal;
import java.sql.Blob;

@Component
public class ItemAddClassBuilder extends ClassBuilderPattern<Item> {
    protected ItemAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new Item());
    }

    @Override
    public ItemAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public ItemAddClassBuilder cloneObject(Item entity) {
        super.cloneObject(entity);
        return this;
    }

    public ItemAddClassBuilder withName (String name){
        super.entity.setName(name);
        return this;
    }

    public ItemAddClassBuilder withDescription (String description){
        super.entity.setDescription(description);
        return this;
    }

    public ItemAddClassBuilder withPrice (BigDecimal price){
        super.entity.setPrice(price);
        return this;
    }

    public ItemAddClassBuilder withPosition(long position){
        super.entity.setPosition(position);
        return this;
    }

    public ItemAddClassBuilder withImageUrl (String image){
        super.entity.setImageUrl(image);
        return this;
    }

    public ItemAddClassBuilder withCategory(Category category){
        super.entity.setCategory(category);
        return this;
    }
}
