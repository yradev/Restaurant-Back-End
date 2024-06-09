package source.restaurant_web_project.models.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ItemEditDTO {
    @Min(1)
    private long position;
    @Size(min = 3, max = 20, message = "Item name must be between 3 and 20 chars!")
    private String name;
    @Size(max = 30,message = "Description must be max 30 symbols")
    private String description;

    @Positive(message = "Price must be more than 0!")
    @NotNull(message = "Price must be more than 0")
    private BigDecimal price;

    public ItemEditDTO(){};

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

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
}
