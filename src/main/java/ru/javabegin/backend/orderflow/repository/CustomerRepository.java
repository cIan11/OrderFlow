package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.entity.Tenant;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByTenantId(Long tenantId);
}
