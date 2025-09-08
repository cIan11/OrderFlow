package ru.javabegin.backend.orderflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product extends  BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Tenant tenant;

    private String name;

    private BigDecimal price;

    private String category;

    @Override
    public String toString() {
        return name;
    }
}
