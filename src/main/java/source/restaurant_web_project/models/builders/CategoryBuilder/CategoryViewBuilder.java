package source.restaurant_web_project.models.builders.CategoryBuilder;


import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import source.restaurant_web_project.models.builders.ClassBuilderPattern;
import source.restaurant_web_project.models.dto.category.CategoryViewDTO;

import java.math.BigDecimal;

public class CategoryViewBuilder extends ClassBuilderPattern<CategoryViewDTO> {
    protected CategoryViewBuilder(ModelMapper modelMapper) {
        super(modelMapper, new CategoryViewDTO());
    }

    @Override
    public CategoryViewBuilder cloneObject(CategoryViewDTO entity) {
        super.cloneObject(entity);
        return this;
    }

    @Override
    public CategoryViewBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    public CategoryViewBuilder withName(String name){
        super.entity.setName(name);
        return this;
    }

    public CategoryViewBuilder withDescription(String description){
        super.entity.setDescription(description);
        return this;
    }

    public CategoryViewBuilder withPosition(int position){
        super.entity.setPosition(position);
        return this;
    }

    public CategoryViewBuilder withImage(String image){
        super.entity.setImageUrl(image);
        return this;
    }

    public CategoryViewBuilder withItemsCount(int count){
        super.entity.setCountItems(count);
        return this;
    }

    public CategoryViewBuilder withTotalPrice(BigDecimal price){
        super.entity.setTotalPrice(price);
        return this;
    }
}
