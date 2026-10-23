package ru.com.orderplatform.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.enums.OrdersStatus;
import ru.com.orderplatform.exception.OrderNotFoundException;
import ru.com.orderplatform.mapper.OrdersMapper;
import ru.com.orderplatform.model.OrdersEntity;
import ru.com.orderplatform.repository.OrdersRepository;

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrdersMapper ordersMapper;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    @Test
    void create_shouldSaveEntityAndReturnId() {
        // Arrange
        OrdersDto dto = new OrdersDto("Laptop", 2, new BigDecimal("999.99"), OrdersStatus.NEW);
        UUID expectedId = UUID.randomUUID();
        OrdersEntity entity = new OrdersEntity();
        entity.setId(expectedId);
        when(ordersMapper.toEntity(dto)).thenReturn(entity);
        when(ordersRepository.save(entity)).thenReturn(entity);

        // Act
        UUID actualId = ordersService.create(dto);

        // Assert
        assertThat(actualId).isEqualTo(expectedId);
        verify(ordersMapper).toEntity(dto);
        verify(ordersRepository).save(entity);
    }

    @Test
    void getOrder_shouldReturnMappedResponse_whenOrderExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrdersEntity entity = new OrdersEntity();
        entity.setId(orderId);
        entity.setName("Phone");
        entity.setQuantity(1);
        entity.setPrice(new BigDecimal("500.00"));
        entity.setStatus(OrdersStatus.NEW);
        entity.setCreated(LocalDateTime.of(2026, 10, 23, 12, 0));

        OrdersResponse expected = new OrdersResponse(
                orderId,
                "Phone",
                1,
                new BigDecimal("500.00"),
                OrdersStatus.NEW,
                LocalDateTime.of(2026, 10, 23, 12, 0)
        );
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(entity));
        when(ordersMapper.toDto(entity)).thenReturn(expected);

        // Act
        OrdersResponse actual = ordersService.getOrder(orderId);

        // Assert
        assertThat(actual).isEqualTo(expected);
        verify(ordersRepository).findById(orderId);
        verify(ordersMapper).toDto(entity);
    }

    @Test
    void getOrder_shouldThrowOrderNotFoundException_whenOrderMissing() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThatThrownBy(() -> ordersService.getOrder(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(String.valueOf(orderId));
    }

    @Test
    void getOrders_shouldReturnMappedPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        OrdersEntity entity = new OrdersEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Tablet");
        entity.setQuantity(3);
        entity.setPrice(new BigDecimal("300.00"));
        entity.setStatus(OrdersStatus.SEND);
        entity.setCreated(LocalDateTime.of(2026, 10, 23, 15, 0));

        OrdersResponse response = new OrdersResponse(
                entity.getId(),
                "Tablet",
                3,
                new BigDecimal("300.00"),
                OrdersStatus.SEND,
                LocalDateTime.of(2026, 10, 23, 15, 0)
        );
        when(ordersRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(ordersMapper.toDto(entity)).thenReturn(response);

        // Act
        Page<OrdersResponse> actual = ordersService.getOrders(pageable);

        // Assert
        assertThat(actual.getContent()).containsExactly(response);
        assertThat(actual.getTotalElements()).isEqualTo(1);
        verify(ordersRepository).findAll(pageable);
        verify(ordersMapper).toDto(any(OrdersEntity.class));
    }
}
