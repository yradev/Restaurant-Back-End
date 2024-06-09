package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.models.builders.DeliveryBuilder.DeliveryBuilder;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;
import source.restaurant_web_project.models.entity.*;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.repositories.*;
import source.restaurant_web_project.services.DeliveryService;

import java.math.BigDecimal;
import java.util.*;

import static source.restaurant_web_project.models.entity.enums.DeliveryStatus.*;

@Service
public class DeliveryServiceIMPL implements DeliveryService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryItemRepository deliveryItemRepository;

    private final DeliveryBuilder deliveryBuilder;


    public DeliveryServiceIMPL(UserRepository userRepository, ModelMapper modelMapper, DeliveryRepository deliveryRepository, DeliveryItemRepository deliveryItemRepository, DeliveryBuilder deliveryBuilder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.deliveryRepository = deliveryRepository;
        this.deliveryItemRepository = deliveryItemRepository;
        this.deliveryBuilder = deliveryBuilder;
    }

    @Override
    public void addNewDelivery(AddDeliveryDTO addDeliveryDTO) {
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        double totalPrice = addDeliveryDTO.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice().multiply(item.getCount()).doubleValue())
                .sum();

        Delivery newDelivery = deliveryBuilder.buildDelivery()
                .mapToClass(addDeliveryDTO)
                .withReceiveTime(addDeliveryDTO.getReceivedTime())
                .withDeliver(user)
                .withStatus(PENDING)
                .withTotalPrice(BigDecimal.valueOf(totalPrice))
                .build();

        Delivery delivery = deliveryRepository.saveAndFlush(newDelivery);


        List<DeliveryItem> deliveryItems = addDeliveryDTO.getItems().stream()
                .map(a -> {
                    DeliveryItem tempDeliveryItem = modelMapper.map(a, DeliveryItem.class);
                    tempDeliveryItem.setDelivery(delivery);
                    return tempDeliveryItem;
                })
                .toList();

        deliveryItemRepository.saveAllAndFlush(deliveryItems);
    }

    @Override
    public DeliveryViewDTO getActiveDelivery() {
        return this.getDeliveriesForAuthenticatedUser().stream()
                .filter(a -> a.getStatus() == DeliveryStatus.PENDING || a.getStatus() == DeliveryStatus.TRAVELLING || a.getStatus() == DeliveryStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<DeliveryViewDTO> getDeliveriesForAuthenticatedUser() {
        return userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .getDeliveries().stream()
                .map(a -> deliveryBuilder.buildDeliveryView()
                        .mapToClass(a)
                        .build())
                .toList();
    }

    @Override
    public List<DeliveryViewDTO> getHistoryForStaff() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> deliveryBuilder.buildDeliveryView()
                        .mapToClass(delivery)
                        .build())
                .filter(a -> a.getStatus() != PENDING && a.getStatus() != ACCEPTED && a.getStatus() != TRAVELLING)
                .sorted((a, b) -> b.getReceiveTime().compareTo(a.getReceiveTime()))
                .limit(180)
                .toList();
    }

    @Override
    public void removeDelivery(long deliveryId) {
        Delivery delivery = deliveryRepository.findDeliveryById(deliveryId);

        if (delivery == null) {
            throw new NotFoundException();
        }

        deliveryRepository.delete(delivery);
    }

    @Override
    public List<DeliveryViewDTO> getHistoryForAuthUser() {
        return this.getDeliveriesForAuthenticatedUser().stream()
                .filter(a -> a.getStatus() == DELIVERED || a.getStatus() == CANCELED)
                .toList();
    }

    @Override
    public List<DeliveryViewDTO> getActiveDeliveriesForStaff() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> deliveryBuilder.buildDeliveryView()
                        .mapToClass(delivery)
                        .withUsername(delivery.getDeliver().getEmail())
                        .build())
                .filter(a -> a.getStatus() != DELIVERED && a.getStatus() != CANCELED)
                .sorted((a, b) -> b.getReceiveTime().compareTo(a.getReceiveTime()))
                .limit(180)
                .toList();
    }

    @Override
    public void changeStatusForStaff(long id, String status) {
        this.changeStatus(id, status);
    }

    @Override
    public void changeStatusForAuthUser(long id, String status) {
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getDeliveries().stream()
                .anyMatch(a -> a.getId() == id)) {
            this.changeStatus(id, status);
        } else {
            throw new UnauthorizedException();
        }
    }

    private void changeStatus(long deliveryID, String status) {
        Delivery currDelivery = deliveryRepository.findDeliveryById(deliveryID);
        if (currDelivery == null) {
            throw new NotFoundException();
        }

        Delivery delivery = deliveryBuilder.buildDelivery()
                .cloneObject(currDelivery)
                .withStatus(DeliveryStatus.valueOf(status))
                .build();

        deliveryRepository.saveAndFlush(delivery);
    }
}