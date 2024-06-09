package source.restaurant_web_project.models.dto.delivery;

import java.math.BigDecimal;

public class DeliveryItemAddDTO {
    private String categoryName;
    private String itemName;
    private BigDecimal count;

    private BigDecimal price;

    public DeliveryItemAddDTO(){}

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}
