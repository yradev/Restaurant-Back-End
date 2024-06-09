package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.builders.DeliveryBuilder.DeliveryBuilder;
import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryItemAddDTO;
import source.restaurant_web_project.models.entity.*;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceIMPLTest {

    private DeliveryServiceIMPL test;

    @Mock
    private DeliveryItemRepository deliveryItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    void setUp() {
        DeliveryBuilder deliveryBuilder = new DeliveryBuilder(new ModelMapper());
        test = new DeliveryServiceIMPL(userRepository, modelMapper, deliveryRepository, deliveryItemRepository, deliveryBuilder);
    }

    @Test
    void addNewDelivery() {
        AddDeliveryDTO addDeliveryDTO = new AddDeliveryDTO();

        addDeliveryDTO.setReceivedTime(LocalDateTime.now());
        DeliveryItemAddDTO item = new DeliveryItemAddDTO();
        item.setPrice(BigDecimal.valueOf(2));
        item.setCount(BigDecimal.valueOf(1));

        addDeliveryDTO.setItems(List.of(item));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(any())).thenReturn(new User());

        test.addNewDelivery(addDeliveryDTO);
        verify(deliveryRepository, times(1)).saveAndFlush(any());
        verify(deliveryItemRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    void getActiveDelivery(){
       this.getDeliveriesForAuthenticatedUser();
       Assertions.assertThrows(NotFoundException.class,()->test.getActiveDelivery());
    }

    @Test
    void getDeliveriesForAuthenticatedUser() {
        User user = new User();
        Delivery delivery = new Delivery();
        delivery.setId(1);
        delivery.setReceiveTime(LocalDateTime.now());
        user.setDeliveries(List.of(delivery));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Assertions.assertEquals(test.getDeliveriesForAuthenticatedUser().get(0).getId(), 1);
    }

    @Test
    void getHistoryForStaff(){
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setReceiveTime(LocalDateTime.now());

        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));
        Assertions.assertEquals(test.getHistoryForStaff().size(),1);
    }

    @Test
    void getActiveDeliveriesForStaff() {
        Delivery delivery = new Delivery();

        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setReceiveTime(LocalDateTime.now());

        Assertions.assertEquals(test.getActiveDeliveriesForStaff().size(),1);
    }

    @Test
    void changeStatusForAuthUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Delivery delivery = new Delivery();
        delivery.setId(2L);

        User user = new User();
        user.setDeliveries(List.of(delivery));

        when(userRepository.findUserByEmail(any())).thenReturn(user);

        Assertions.assertThrows(UnauthorizedException.class,()->test.changeStatusForAuthUser(1L,"PENDING"));

        user.getDeliveries().get(0).setId(1L);

        Assertions.assertThrows(NotFoundException.class,()->test.changeStatusForAuthUser(1L,"PENDING"));
        when(deliveryRepository.findDeliveryById(1)).thenReturn(new Delivery());

        test.changeStatusForAuthUser(1, "PENDING");
        verify(deliveryRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void changeStatusForStaff(){
        Assertions.assertThrows(NotFoundException.class,()->test.changeStatusForStaff(1L,"PENDING"));
        when(deliveryRepository.findDeliveryById(1)).thenReturn(new Delivery());

        test.changeStatusForStaff(1, "PENDING");
        verify(deliveryRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void removeDelivery(){
        Assertions.assertThrows(NotFoundException.class,()->test.removeDelivery(1L));
        when(deliveryRepository.findDeliveryById(1L)).thenReturn(new Delivery());

        test.removeDelivery(1L);
        verify(deliveryRepository,times(1)).delete(any());

    }

    @Test
    void getHistoryForAuthUser (){
        this.getDeliveriesForAuthenticatedUser();
        Assertions.assertTrue(test.getHistoryForAuthUser().isEmpty());
    }
}