package ru.javabegin.backend.orderflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.repository.CustomerRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(Long tenantId) {
        return customerRepository.findByTenantId(tenantId);
    }
}
