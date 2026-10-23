package ru.com.orderplatform.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import ru.com.orderplatform.enums.OrdersStatus;

public record OrdersDto(@NotBlank String name,
                        @Positive int quantity,
                        @NotNull @DecimalMin("0.01") BigDecimal price,
                        OrdersStatus status) {
}
