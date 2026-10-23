package ru.com.orderplatform.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.enums.OrdersStatus;
import ru.com.orderplatform.model.OrdersEntity;

class OrdersMapperTest {

    private OrdersMapper ordersMapper;

    @BeforeEach
    void setUp() {
        // Arrange (shared)
        ordersMapper = new OrdersMapperImpl();
    }

    @Test
    void toEntity_shouldMapFieldsAndIgnoreId() {
        // Arrange
        OrdersDto dto = new OrdersDto("Laptop", 2, new BigDecimal("999.99"), OrdersStatus.NEW);

        // Act
        OrdersEntity entity = ordersMapper.toEntity(dto);

        // Assert
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("Laptop");
        assertThat(entity.getQuantity()).isEqualTo(2);
        assertThat(entity.getPrice()).isEqualByComparingTo("999.99");
        assertThat(entity.getStatus()).isEqualTo(OrdersStatus.NEW);
        assertThat(entity.getCreated()).isNull();
    }

    @Test
    void toDto_shouldMapEntityToResponse() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.of(2026, 10, 23, 12, 0);
        OrdersEntity entity = new OrdersEntity();
        entity.setId(orderId);
        entity.setName("Phone");
        entity.setQuantity(1);
        entity.setPrice(new BigDecimal("500.00"));
        entity.setStatus(OrdersStatus.SUCCESS);
        entity.setCreated(created);

        // Act
        OrdersResponse response = ordersMapper.toDto(entity);

        // Assert
        assertThat(response).isEqualTo(new OrdersResponse(
                orderId,
                "Phone",
                1,
                new BigDecimal("500.00"),
                OrdersStatus.SUCCESS,
                created
        ));
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        // Arrange
        OrdersDto dto = null;

        // Act
        OrdersEntity entity = ordersMapper.toEntity(dto);

        // Assert
        assertThat(entity).isNull();
    }

    @Test
    void toDto_shouldReturnNull_whenEntityIsNull() {
        // Arrange
        OrdersEntity entity = null;

        // Act
        OrdersResponse response = ordersMapper.toDto(entity);

        // Assert
        assertThat(response).isNull();
    }
}
