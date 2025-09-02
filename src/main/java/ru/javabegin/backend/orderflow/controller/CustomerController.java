package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.service.CustomerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("tenants/{tenantId}/customers")
public class CustomerController {
    private CustomerService customerService;

    //1) Получить пользователей для магазина
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(@PathVariable Long tenantId){
        return ResponseEntity.ok(customerService.getAllCustomers(tenantId));
    }
    //2) Получить пользователя по параметрам (email || phone)

    //3) Получить пользователя по id

    //4) Создать пользователя

    //5) Изменить пользователя

    //6) Удалить пользователя

    //7) Получить последние n заказов покупателя
}
