package ru.javabegin.backend.orderflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.backend.orderflow.entity.Product;
import ru.javabegin.backend.orderflow.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //CRUD для продукта

    //1) Получение всех продуктов

    //2) Получение продукта по id

    //2) Добавление продукта

    //3) Изменение продукта

    //4) Удаление продукта

    //Поиск по категории
}
