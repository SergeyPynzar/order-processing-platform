package ru.com.orderplatform.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.com.orderplatform.dto.OrdersResponse;
import ru.com.orderplatform.dto.OrdersDto;
import ru.com.orderplatform.exception.OrderNotFoundException;
import ru.com.orderplatform.mapper.OrdersMapper;
import ru.com.orderplatform.model.OrdersEntity;
import ru.com.orderplatform.repository.OrdersRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;

    @Override
    public UUID create(OrdersDto ordersDto) {
        log.info("Начало процесса сохранения заказа");
        OrdersEntity entity = ordersMapper.toEntity(ordersDto);
        ordersRepository.save(entity);
        log.info("Заказ с orderId: {} успешно сохранен", entity.getId());
        return entity.getId();
    }

    @Override
    public OrdersResponse getOrder(UUID orderId) {
        log.info("Получение заказа с orderId: {}", orderId);
        OrdersEntity entity = ordersRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException(String.valueOf(orderId)));
        return ordersMapper.toDto(entity);
    }

    @Override
    public Page<OrdersResponse> getOrders(Pageable pageable) {
        log.info("Получение всех заказов");
        return ordersRepository.findAll(pageable)
                .map(ordersMapper::toDto);
    }
}
