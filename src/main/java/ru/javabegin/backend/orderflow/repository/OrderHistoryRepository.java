package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabegin.backend.orderflow.entity.OrderHistory;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderId(Long orderId);

}
