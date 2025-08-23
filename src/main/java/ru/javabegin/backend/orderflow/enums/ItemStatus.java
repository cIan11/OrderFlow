package ru.javabegin.backend.orderflow.enums;

public enum ItemStatus {
    RESERVED,    // Товар зарезервирован
    SHIPPED,     // Товар отгружен (может быть частичная отгрузка)
    DELIVERED,   // Товар доставлен
    RETURNED,    // Товар возвращен
    CANCELLED    // Позиция отменена
}
