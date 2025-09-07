package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.entity.Order;
import ru.javabegin.backend.orderflow.entity.OrderHistory;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.enums.OrderStatus;
import ru.javabegin.backend.orderflow.service.CustomerService;
import ru.javabegin.backend.orderflow.service.OrderService;
import ru.javabegin.backend.orderflow.service.TenantService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tenants/{tenantId}/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;
    private CustomerService customerService;
    private TenantService tenantService;

    //1) Получить все заказы (с фильтрами по статусу, дате, клиенту)
    @GetMapping("/all")
    public ResponseEntity<?> getOrders(
            @PathVariable Long tenantId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) String status) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверка customer если указан
        if (customerId != null && !customerService.existsByTenantIdAndId(tenantId, customerId)) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        // Проверка статуса если указан
        if (status != null) {
            try {
                OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Invalid status. Valid values: " +
                        Arrays.toString(OrderStatus.values()), HttpStatus.BAD_REQUEST);
            }
        }

        return ResponseEntity.ok(orderService.getOrdersByParams(tenantId, status, customerId, dateFrom, dateTo));
    }

    //2) Получить заказ по id
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @PathVariable Long tenantId,
            @PathVariable Long orderId) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        try {
            Order order = orderService.getOrder(tenantId, orderId);
            return ResponseEntity.ok(order);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    //3) Создать заказ (включая позиции)
    @PostMapping
    public ResponseEntity<?> createOrder(
            @PathVariable Long tenantId,
            @RequestBody Order order) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверки обязательных полей
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            return new ResponseEntity<>("Customer is required", HttpStatus.BAD_REQUEST);
        }

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return new ResponseEntity<>("Order must contain at least one item", HttpStatus.BAD_REQUEST);
        }

        // Проверка существования customer и его принадлежности к tenant
        Long customerId = order.getCustomer().getId();
        if (!customerService.existsByTenantIdAndId(tenantId, customerId)) {
            return new ResponseEntity<>("Customer not found or doesn't belong to tenant", HttpStatus.NOT_FOUND);
        }

        // Установка tenant и дат
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        order.setTenant(tenant);

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Расчет общей суммы
        BigDecimal totalPrice = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderService.save(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    //4) Обновить заказ (адрес доставки, клиент)
    @PutMapping
    public ResponseEntity<?> updateOrder(
            @PathVariable Long tenantId,
            @RequestBody Order order) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверки обязательных полей
        if (order.getId() == null) {
            return new ResponseEntity<>("Order ID is required", HttpStatus.BAD_REQUEST);
        }

        // Проверка существования order и его принадлежности к tenant
        if (!orderService.existsByTenantIdAndId(tenantId, order.getId())) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        // Получаем существующий заказ
        Order existingOrder = orderService.getOrder(tenantId, order.getId());

        // Обновляем разрешенные поля
        if (order.getCustomer() != null && order.getCustomer().getId() != null) {
            Long newCustomerId = order.getCustomer().getId();
            if (!customerService.existsByTenantIdAndId(tenantId, newCustomerId)) {
                return new ResponseEntity<>("New customer not found", HttpStatus.NOT_FOUND);
            }

            Customer newCustomer = new Customer();
            newCustomer.setId(newCustomerId);
            existingOrder.setCustomer(newCustomer);
        }

        existingOrder.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderService.save(existingOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    //5) Сменить статус (с записью в историю)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long tenantId,
            @PathVariable Long orderId,
            @RequestParam String newStatus) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверка существования order
        if (!orderService.existsByTenantIdAndId(tenantId, orderId)) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        // Проверка валидности статуса
        try {
            OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status. Valid values: " +
                    Arrays.toString(OrderStatus.values()), HttpStatus.BAD_REQUEST);
        }

        Order order = orderService.getOrder(tenantId, orderId);
        String oldStatus = order.getStatus().name();

        // Сохраняем историю перед изменением статуса
        orderService.createOrderHistory(order, oldStatus, newStatus);

        // Обновляем статус
        Order updatedOrder = orderService.updateOrderStatus(order, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    //6) Удалить/отменить заказ
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable Long tenantId,
            @PathVariable Long orderId) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверка существования order
        if (!orderService.existsByTenantIdAndId(tenantId, orderId)) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        orderService.delete(tenantId, orderId);
        return ResponseEntity.ok().build();
    }

    //7) Получить историю заказа
    @GetMapping("/{orderId}/history")
    public ResponseEntity<?> getOrderHistory(
            @PathVariable Long tenantId,
            @PathVariable Long orderId) {

        // Проверка существования tenant
        if (!tenantService.existsById(tenantId)) {
            return new ResponseEntity<>("Tenant not found", HttpStatus.NOT_FOUND);
        }

        // Проверка существования order
        if (!orderService.existsByTenantIdAndId(tenantId, orderId)) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        List<OrderHistory> history = orderService.getOrderHistory(orderId);
        return ResponseEntity.ok(history);
    }
}