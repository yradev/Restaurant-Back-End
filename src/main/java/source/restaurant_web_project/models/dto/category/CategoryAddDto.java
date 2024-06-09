package source.restaurant_web_project.models.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryAddDto {
    @NotNull
    @NotBlank
    @Size(min = 3,max = 20,message = "Category name must be between 3 and 20 chars!")
    private String name;

    private String description;

    public CategoryAddDto(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
