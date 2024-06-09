package source.restaurant_web_project.models.dto.delivery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AddDeliveryDTO {
    private String address;
    private String phoneNumber;
    private LocalDateTime receivedTime;

    private List<DeliveryItemAddDTO> items;

    public AddDeliveryDTO(){}

    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }

    public List<DeliveryItemAddDTO> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemAddDTO> items) {
        this.items = items;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
