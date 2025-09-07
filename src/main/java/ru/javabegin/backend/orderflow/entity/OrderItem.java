package ru.javabegin.backend.orderflow.entity;

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
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer quantity;

    //цена на момент покупки
    private BigDecimal price;

    @Column(name = "item_status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
}
