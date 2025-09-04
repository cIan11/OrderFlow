package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.orderflow.entity.Order;
import ru.javabegin.backend.orderflow.service.OrderService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tenants/{tenantId}/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;
    //1) Получить все заказы (с фильтрами по статусу, дате, клиенту)
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getOrders(
            @PathVariable Long tenantId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(orderService.getAllOrders(tenantId,customerId, dateFrom, dateTo,status));
    }

    //2) Получить заказ по id
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long tenantId,
            @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrder(tenantId,orderId));
    }

    //3) Создать заказ (включая позиции)
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(
            @PathVariable Long tenantId,
            @RequestBody Order order){
        //Проверки обязательных полей
        //Присвоение к покупателю + проверка/присвоение к магазину
    }

    //4) Обновить заказ (адрес доставки, клиент)
    @PutMapping("/update")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long tenantId,
            @RequestBody Order order){
        //Проверки обязательных полей
        //Проверка соответствия id customer order tenant
    }

    //5) Сменить статус (с записью в историю)
    @PatchMapping("/update/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long tenantId,
            @RequestParam String newStatus){
        //Проверка, что cutomer, tenant и order существуют и связаны
        //Новый статус должен соответствовать 1 из enum, иначе выбрасываем ошибку + возможные варианты

    }

    //6) Удалить/отменить заказ
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Order> deleteOrder(
            @PathVariable Long tenantId,
            @PathVariable Long orderId){

    }


    //7) Получить историю заказа
    @GetMapping("/history/{orderId}")
    public ResponseEntity<List<Order>> getOrderHistory(
            @PathVariable Long tenantId,
            @PathVariable Long orderId){

    }
}
