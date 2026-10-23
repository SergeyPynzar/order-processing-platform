package ru.com.orderplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.dto.OrdersPageResponse;
import ru.com.orderplatform.dto.OrdersResponse;

public interface OrdersControllerApi {
    @Operation(summary = "Создать заказ")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Заказ успешно создан",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UUID.class, format = "uuid")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверно переданы параметры в теле запроса",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            }
    )
    ResponseEntity<UUID> createOrder(OrdersDto ordersDto);

    @Operation(summary = "Получить заказ")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказ успешно найден",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrdersResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Заказ не найден по id",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            }
    )
    OrdersResponse getOrder(UUID orderId);

    @Operation(summary = "Получить список заказов")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказы успешно возвращены",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrdersPageResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
            }
    )
    Page<OrdersResponse> getOrders(@PageableDefault(size = 20, sort = "created", direction = Sort.Direction.DESC)
                                 Pageable pageable);
}
