package ru.com.orderplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import ru.com.orderplatform.enums.OrdersStatus;

public record OrdersResponse(UUID id,
                             String name,
                             int quantity,
                             BigDecimal price,
                             OrdersStatus status,
                             LocalDateTime created) {
}
