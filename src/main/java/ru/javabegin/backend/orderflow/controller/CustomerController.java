package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.service.CustomerService;

import java.util.List;
import java.util.NoSuchElementException;

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
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable Long tenantId,
            @PathVariable Long id){
        Customer customer = customerService.findById(tenantId,id);
        return ResponseEntity.ok(customer);
    }

    //4) Создать пользователя
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(
            @PathVariable Long tenantId,
            @RequestBody Customer customer){
        //Проверки
        if (customer.getId() != null && customer.getId() != 0){
            return new ResponseEntity("redundant param: id MUST be null",HttpStatus.BAD_REQUEST);
        }
        if(customer.getName()==null || customer.getName().trim().isEmpty()) {
            return new ResponseEntity("Tenant name must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (customer.getEmail()==null || customer.getEmail().trim().isEmpty()) {
            return new ResponseEntity("Tenant email must not be empty",HttpStatus.BAD_REQUEST);
        }
        //Нужен ли телефон?
        //Присвоение тенанта
        try {
            Customer savedCustomer = customerService.createCustomer(tenantId, customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("Tenant not found",HttpStatus.NOT_FOUND);
        }
    }


    //5) Изменить пользователя
    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long tenantId,
            @RequestBody Customer customer){
        //Проверки
        if (customer.getId() == null){
            return new ResponseEntity("ID must be provided for update",HttpStatus.BAD_REQUEST);
        }
        if(customer.getName()==null || customer.getName().trim().isEmpty()) {
            return new ResponseEntity("Tenant name must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (customer.getEmail()==null || customer.getEmail().trim().isEmpty()) {
            return new ResponseEntity("Tenant email must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (!customer.getTenant().getId().equals(tenantId)) {
            return new ResponseEntity("Customer does not belong to this tenant", HttpStatus.BAD_REQUEST);
        }

        Customer existingCustomer = customerService.findById(tenantId, customer.getId());
        if (!existingCustomer.getTenant().getId().equals(tenantId)) {
            return new ResponseEntity("Customer does not belong to this tenant", HttpStatus.BAD_REQUEST);
        }

        // Обновляем поля (кроме tenant)
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());

        Customer updatedCustomer = customerService.save(existingCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    //6) Удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(
            @PathVariable Long tenantId,
            @PathVariable Long id){
        try {
            customerService.delete(tenantId,id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    //7) Получить последние n заказов покупателя
    //Где вообще прописывать?
}
