package ru.com.orderplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "OrdersPageResponse", description = "Страница заказов (Spring Data Page)")
public record OrdersPageResponse(
        @Schema(description = "Элементы текущей страницы")
        List<OrdersResponse> content,
        @Schema(description = "Общее количество элементов")
        long totalElements,
        @Schema(description = "Общее количество страниц")
        int totalPages,
        @Schema(description = "Размер страницы")
        int size,
        @Schema(description = "Номер текущей страницы (с нуля)")
        int number,
        @Schema(description = "Количество элементов на текущей странице")
        int numberOfElements,
        @Schema(description = "Первая страница")
        boolean first,
        @Schema(description = "Последняя страница")
        boolean last,
        @Schema(description = "Страница пустая")
        boolean empty
) {
}
