package ru.com.orderplatform.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.dto.OrdersDto;

public interface OrdersService {
    /**
     * Создание нового заказа
     * @param ordersDto - заказ
     * @return id - идентификатор заказа
     */
    UUID create(OrdersDto ordersDto);

    /**
     * Полученеие заказа по orderId
     * @param orderId - идентификатор заказа
     * @return OrderResponse - заказ
     */
    OrdersResponse getOrder(UUID orderId);

    /**
     * Полученеие всех заказов
     * @return Page<OrderResponse> - список заказо
     */
    Page<OrdersResponse> getOrders(Pageable pageable);
}
