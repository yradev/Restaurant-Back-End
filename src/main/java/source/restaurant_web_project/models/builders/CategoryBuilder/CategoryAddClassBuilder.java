package source.restaurant_web_project.models.builders.CategoryBuilder;

import org.modelmapper.ModelMapper;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;

import java.sql.Blob;
import java.util.List;

public class CategoryAddClassBuilder extends ClassBuilderPattern<Category> {
    public CategoryAddClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new Category());
    }

    @Override
    public CategoryAddClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public CategoryAddClassBuilder cloneObject(Category entity) {
        super.cloneObject(entity);
        return this;
    }

    public CategoryAddClassBuilder withId(long id) {
        super.entity.setId(id);
        return this;
    }

    public CategoryAddClassBuilder withName(String name) {
        super.entity.setName(name);
        return this;
    }

    public CategoryAddClassBuilder withDescription(String description) {
        super.entity.setDescription(description);
        return this;
    }

    public CategoryAddClassBuilder withPosition(long position) {
        super.entity.setPosition(position);
        return this;
    }

    public CategoryAddClassBuilder withImageUrl(String imageUrl) {
        super.entity.setImageUrl(imageUrl);
        return this;
    }

    public CategoryAddClassBuilder withItems(List<Item> items) {
        super.entity.setItems(items);
        return this;
    }
}
