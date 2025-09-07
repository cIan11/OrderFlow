package ru.javabegin.backend.orderflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javabegin.backend.orderflow.enums.ItemStatus;
import ru.javabegin.backend.orderflow.enums.OrderStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
    //Нужно для сохранения цены товара на момент покупки.
    //Учитываем, что стоимость товара может поменяться

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer quantity;

    //цена на момент покупки
    @Column(name = "price")
    private BigDecimal purchasePrice;

    @Column(name = "item_status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    // Метод для получения актуальной цены
    public BigDecimal getEffectivePrice() {
        if (order != null && order.getStatus() == OrderStatus.CART) {
            // Для корзины - текущая цена продукта
            return product != null ? product.getPrice() : BigDecimal.ZERO;
        } else {
            // Для заказа - зафиксированная цена
            return purchasePrice != null ? purchasePrice : BigDecimal.ZERO;
        }
    }

    // Метод для получения общей стоимости позиции
    public BigDecimal getTotalPrice() {
        BigDecimal price = getEffectivePrice();
        Integer qty = quantity != null ? quantity : 0;
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
