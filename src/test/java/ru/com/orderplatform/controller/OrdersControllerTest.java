package ru.com.orderplatform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.enums.OrdersStatus;
import ru.com.orderplatform.exception.GlobalExceptionHandler;
import ru.com.orderplatform.exception.OrderNotFoundException;
import ru.com.orderplatform.service.OrdersService;

@WebMvcTest(controllers = {OrdersController.class, GlobalExceptionHandler.class})
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdersService ordersService;

    @Test
    void createOrder_shouldReturnCreatedWithId() throws Exception {
        // Arrange
        UUID orderId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(ordersService.create(any(OrdersDto.class))).thenReturn(orderId);

        // Act / Assert
        mockMvc.perform(post("/v1/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Laptop",
                                  "quantity": 2,
                                  "price": 999.99,
                                  "status": "NEW"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + orderId + "\""));
    }

    @Test
    void createOrder_shouldReturnBadRequest_whenBodyInvalid() throws Exception {
        // Arrange
        String invalidBody = """
                {
                  "name": "",
                  "quantity": -1,
                  "price": 0,
                  "status": "NEW"
                }
                """;

        // Act / Assert
        mockMvc.perform(post("/v1/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_shouldReturnOrder_whenExists() throws Exception {
        // Arrange
        UUID orderId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        OrdersResponse response = new OrdersResponse(
                orderId,
                "Phone",
                1,
                new BigDecimal("500.00"),
                OrdersStatus.NEW,
                LocalDateTime.of(2026, 10, 23, 12, 0)
        );
        when(ordersService.getOrder(orderId)).thenReturn(response);

        // Act / Assert
        mockMvc.perform(get("/v1/api/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.price").value(500.00))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrder_shouldReturnNotFound_whenOrderMissing() throws Exception {
        // Arrange
        UUID orderId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        when(ordersService.getOrder(orderId)).thenThrow(new OrderNotFoundException(orderId.toString()));

        // Act / Assert
        mockMvc.perform(get("/v1/api/orders/{orderId}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found."));
    }

    @Test
    void getOrders_shouldReturnPage() throws Exception {
        // Arrange
        UUID orderId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        OrdersResponse response = new OrdersResponse(
                orderId,
                "Tablet",
                3,
                new BigDecimal("300.00"),
                OrdersStatus.SEND,
                LocalDateTime.of(2026, 10, 23, 15, 0)
        );
        when(ordersService.getOrders(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        // Act / Assert
        mockMvc.perform(get("/v1/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(orderId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Tablet"))
                .andExpect(jsonPath("$.content[0].status").value("SEND"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
