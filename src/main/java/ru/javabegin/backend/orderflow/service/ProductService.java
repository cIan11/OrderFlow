package ru.javabegin.backend.orderflow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Product;
import ru.javabegin.backend.orderflow.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public List<Product> getAllProducts(Long tenantId) {
        return productRepository.findByTenantIdOrderByNameAsc(tenantId);
    }

    public List<Product> getProductsByCategory(Long tenantId, String category) {
        return productRepository.findByTenantIdAndCategoryOrderByNameAsc(tenantId,category);
    }

    public Product getProductById(Long tenantId, Long productId) {
        return productRepository.findByIdAndTenantId(productId, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Product with id=" + productId + " not found for tenant=" + tenantId));
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProduct(Product product){
        return productRepository.save(product);
    }

    public void deleteProductById(Long tenantId, Long productId){
       Product product = productRepository.findByIdAndTenantId(tenantId,productId).get();
       productRepository.delete(product);
    }

    public boolean existsByIdAndTenantId(Long productId, Long tenantId) {
        return productRepository.existsByIdAndTenantId(productId, tenantId);
    }

}
