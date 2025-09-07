package ru.javabegin.backend.orderflow.enums;

public enum OrderStatus {
    CART,           // В корзине

    NEW,            // Создан
    PROCESSING,     // В обработке
    SHIPPED,        // Отправлен
    DELIVERED,      // Доставлен
    CANCELLED       // Отменен
}
