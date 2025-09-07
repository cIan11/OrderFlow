package ru.javabegin.backend.orderflow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Product;
import ru.javabegin.backend.orderflow.repository.ProductRepository;

import java.util.List;

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

    public Product getProductById(Long productId,Long tenantId){
        return productRepository.findByIdAndTenantId(productId,tenantId).get();
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

}
