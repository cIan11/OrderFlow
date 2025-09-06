package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.orderflow.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o where " +
            "o.tenant.id = :tenantId and" +
            "(:status is null or o.status=:status) and" +
            "(:customerId is null or o.customer.id = :customerId) and" +
            "(:dateFrom is null or o.createdAt>=:dateFrom) and" +
            "(:dateTo is null or o.createdAt<=:dateTo)"
    )
    List<Order> findByParams(
            @Param("tenantId") Long tenantId,
            @Param("status") String status,
            @Param("customerId") Long customerId,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo
    );
    Optional<Order> findByTenantIdAndId(Long tenantId, Long id);

    boolean existsByTenantIdAndId(Long tenantId, Long id);

    List<Order> findByTenantId(Long tenantId);
}
