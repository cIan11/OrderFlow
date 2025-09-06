package ru.javabegin.backend.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.backend.orderflow.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByTenantIdOrderByNameAsc(Long tenantId); //Найти продукты по id магазина
    List<Product> findByTenantIdAndCategoryOrderByNameAsc(Long tenantId, String category); //Найти продукты по id магазина и категории
    Optional<Product> findByIdAndTenantId(Long tenantId, Long productId); //Найти продукт по его id и id магазина
}
