package source.restaurant_web_project.models.dto.item;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;

public class ItemFavoritesDTO {
    private long id;
    private String imageUrl;
    private String itemName;
    private BigDecimal price;
    private String description;
    private long itemPosition;
    private long categoryPosition;
    private String categoryName;

    public  ItemFavoritesDTO(){}

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(long itemPosition) {
        this.itemPosition = itemPosition;
    }

    public long getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(long categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
