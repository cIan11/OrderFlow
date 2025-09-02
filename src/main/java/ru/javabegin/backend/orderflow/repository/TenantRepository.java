package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.orderflow.entity.Tenant;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
        Tenant findByName(String name);
        Tenant findByDomain(String domain);
}
