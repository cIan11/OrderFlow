package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.orderflow.entity.Order;
import ru.javabegin.backend.orderflow.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders o WHERE " +
            "o.tenant_id = :tenantId AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:customerId IS NULL OR o.customer_id = :customerId) AND " +
            "(:dateFrom IS NULL OR o.created_at >= CAST(:dateFrom AS timestamp)) AND " +
            "(:dateTo IS NULL OR o.created_at <= CAST(:dateTo AS timestamp))",
            nativeQuery = true)
    List<Order> findByParamsNative(
            @Param("tenantId") Long tenantId,
            @Param("status") String status,
            @Param("customerId") Long customerId,
            @Param("dateFrom") String dateFrom, // Изменяем на String
            @Param("dateTo") String dateTo      // Изменяем на String
    );
    Optional<Order> findByTenantIdAndId(Long tenantId, Long id);

    boolean existsByTenantIdAndId(Long tenantId, Long id);

}
