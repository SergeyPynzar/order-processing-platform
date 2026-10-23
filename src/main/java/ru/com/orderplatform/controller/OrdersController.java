package ru.com.orderplatform.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.service.OrdersService;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class OrdersController implements OrdersControllerApi {
    private final OrdersService ordersService;

    @Override
    @PostMapping("/orders")
    public ResponseEntity<UUID> createOrder(@Valid @RequestBody OrdersDto ordersDto) {
        UUID orderId = ordersService.create(ordersDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @Override
    @GetMapping("/orders/{orderId}")
    public OrdersResponse getOrder(@PathVariable UUID orderId) {
        return ordersService.getOrder(orderId);
    }

    @Override
    @GetMapping("/orders")
    public Page<OrdersResponse> getOrders(@PageableDefault(size = 20, sort = "created", direction = Sort.Direction.DESC)
                                            Pageable pageable) {
        return ordersService.getOrders(pageable);
    }
}
