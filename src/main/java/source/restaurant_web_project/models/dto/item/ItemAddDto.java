package source.restaurant_web_project.models.dto.item;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ItemAddDto {
    @Size(min = 3, max = 20, message = "Item name must be between 3 and 20 chars!")
    private String name;
    @Size(max = 30,message = "Description must be max 30 symbols")
    private String description;

    @Positive(message = "Price must be more than 0!")
    @NotNull(message = "Price must be more than 0")
    private BigDecimal price;

    private long categoryPosition;

    public ItemAddDto(){};

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(long categoryPosition) {
        this.categoryPosition = categoryPosition;
    }
}
