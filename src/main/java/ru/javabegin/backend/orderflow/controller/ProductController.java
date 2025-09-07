package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.orderflow.entity.Customer;
import ru.javabegin.backend.orderflow.entity.Product;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("tenants/{tenantId}/products")
public class ProductController {

    private ProductService productService;

    //CRUD для продукта

    //1) Получение всех продуктов для 1 магазина
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @PathVariable Long tenantId,
            @RequestParam(required = false) String category) {
        // тернарный оператор - условие ? true : false
        List<Product> products = category != null                             //Если есть категория
                //Проверить категорию?
                ? productService.getProductsByCategory(tenantId, category)    //true получаем продукты по категории
                : productService.getAllProducts(tenantId);                    //false получаем все продукты
        return ResponseEntity.ok(products);
    }

    //2) Получение продукта по id
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long tenantId,
            @PathVariable Long productId){
        Product product = null;
        try {
             product = productService.getProductById(productId,tenantId);
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("id=" + productId + " not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(product);
    }

    //3) Добавление продукта
    @PostMapping
    public ResponseEntity<?> createProduct(
            @PathVariable Long tenantId,
            @RequestBody Product product){
        // Проверка обязательных полей
        if(product.getId()!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id не должен быть указан");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Название продукта обязательно");
        }

        if (product.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Цена продукта обязательна");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Цена должна быть больше 0");
        }

        // Устанавливаем тенанта
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        product.setTenant(tenant);
        //Нужно добавить проверку наличия такого тенанта

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    //4) Изменение продукта
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long tenantId,
            @RequestBody Product product){

        // Проверка обязательных полей
        if(product.getId()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id должен быть указан");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Название продукта обязательно");
        }

        if (product.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Цена продукта обязательна");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка: Цена должна быть больше 0");
        }

        Product existingProduct = productService.getProductById(product.getId(),tenantId);
        if(!existingProduct.getTenant().getId().equals(tenantId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not belong to this tenant");
        }
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());

        Product updatedProduct = productService.updateProduct(existingProduct);

        return ResponseEntity.ok(updatedProduct);
    }

    //5) Удаление продукта
    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(
            @PathVariable Long tenantId,
            @PathVariable Long productId){

        try {
            productService.deleteProductById(tenantId, productId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + productId + " not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    //6) Поиск по категории
}
