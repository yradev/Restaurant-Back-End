package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "delivery_item")
public class DeliveryItem extends BaseEntity {
    private String categoryName;
    private String itemName;
    private long count;
    private BigDecimal price;

    @ManyToOne(targetEntity = Delivery.class)
    private Delivery delivery;

    public DeliveryItem() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
