package ru.javabegin.backend.orderflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.repository.CustomerRepository;
import ru.javabegin.backend.orderflow.repository.TenantRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private TenantRepository tenantRepository;

    public List<Customer> getAllCustomers(Long tenantId) {
        return customerRepository.findByTenantId(tenantId);
    }
    public Customer findById(Long tenantId, Long id){
        return customerRepository.findByTenantIdAndId(tenantId,id)
                .orElseThrow(()->new NoSuchElementException("Customer not found"));
    }
    public Customer createCustomer(Long tenantId, Customer customer) {
        // Находим tenant по ID
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new NoSuchElementException("Tenant not found"));

        customer.setTenant(tenant);
        return customerRepository.save(customer);
    }

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }
    //Оно вообще работает?? Че за дрянь
    public void delete(Long tenantId, Long id){
        Customer customer = customerRepository.findByTenantIdAndId(tenantId, id)
                        .orElseThrow(()->new NoSuchElementException("Customer not found"));
        customerRepository.delete(customer);
    }
}
