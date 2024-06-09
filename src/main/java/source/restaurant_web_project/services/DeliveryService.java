package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;

import java.util.List;

public interface DeliveryService {

    void addNewDelivery(AddDeliveryDTO addDeliveryDTO);

    DeliveryViewDTO getActiveDelivery();
    List<DeliveryViewDTO> getDeliveriesForAuthenticatedUser();

    List<DeliveryViewDTO> getHistoryForStaff();
    void changeStatusForAuthUser(long deliveryID, String status);

    void removeDelivery(long deliveryId);

    List<DeliveryViewDTO> getHistoryForAuthUser();

    List<DeliveryViewDTO> getActiveDeliveriesForStaff();

    void changeStatusForStaff(long id, String status);
}
