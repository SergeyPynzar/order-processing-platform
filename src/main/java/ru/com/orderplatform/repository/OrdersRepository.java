package ru.com.orderplatform.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.com.orderplatform.model.OrdersEntity;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, UUID> {
}
