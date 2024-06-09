package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {
    private LocalDateTime receiveTime;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private BigDecimal totalPrice;

    private String address;

    private String phoneNumber;

    @ManyToOne(targetEntity = User.class)
    private User deliver;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.EAGER, targetEntity = DeliveryItem.class, cascade = CascadeType.REMOVE)
    private List<DeliveryItem> items;

    public Delivery(){}

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public User getDeliver() {
        return deliver;
    }

    public void setDeliver(User deliver) {
        this.deliver = deliver;
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DeliveryItem> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItem> items) {
        this.items = items;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
