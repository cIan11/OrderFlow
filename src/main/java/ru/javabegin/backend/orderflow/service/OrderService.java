package ru.javabegin.backend.orderflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Order;
import ru.javabegin.backend.orderflow.entity.OrderHistory;
import ru.javabegin.backend.orderflow.enums.OrderStatus;
import ru.javabegin.backend.orderflow.repository.OrderHistoryRepository;
import ru.javabegin.backend.orderflow.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderHistoryRepository orderHistoryRepository;

    public List<Order> getOrdersByParams(Long tenantId, String status, Long customerId, LocalDateTime dateFrom, LocalDateTime dateTo){
        return orderRepository.findByParams(tenantId,status,customerId,dateFrom,dateTo);
    }

    public Order findById(Long id, Long tenantId){
        return orderRepository.findById(id).get();
    }
    public Order getOrder(Long tenantId, Long orderId) {
        return orderRepository.findByTenantIdAndId(tenantId, orderId).get();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Long tenantId, Long orderId) {
        Order order = orderRepository.findByTenantIdAndId(tenantId, orderId).get();
        orderRepository.delete(order);
    }

    public Order updateOrderStatus(Order order, String newStatus) {
        order.setStatus(OrderStatus.valueOf(newStatus));
        return orderRepository.save(order);
    }
    public OrderHistory createOrderHistory(Order order, String oldStatus, String newStatus) {
        OrderHistory history = new OrderHistory();
        history.setOrder(order);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedAt(LocalDateTime.now());
        return orderHistoryRepository.save(history);
    }

    public boolean existsByTenantIdAndId(Long tenantId, Long orderId) {
        return orderRepository.existsByTenantIdAndId(tenantId, orderId);
    }

    public List<OrderHistory> getOrderHistory(Long orderId) {
        return orderHistoryRepository.findByOrderId(orderId);
    }

}
