package source.restaurant_web_project.models.dto.delivery;

import source.restaurant_web_project.models.entity.DeliveryItem;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.util.List;

public class DeliveryViewDTO {
    private long id;
    private String receiveTime;
    private DeliveryStatus status;
    private BigDecimal totalPrice;
    private String address;
    private String phoneNumber;
    private String username;
    private List<DeliveryItemViewDTO>items;


    public DeliveryViewDTO(){}

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public List<DeliveryItemViewDTO> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemViewDTO> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
