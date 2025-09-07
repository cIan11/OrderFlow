package ru.javabegin.backend.orderflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends BaseEntity {
    //Tenant = магазин
    //Пробую реализовать логику мультитенатности (multi_tenancy) для хранения всех клиентов в 1 бд, но чтобы
    //они не видели чужую информацию
    private String name;

    private String domain;

    @OneToMany(mappedBy = "tenant")
    //Тут проперти поставить
    private List<Product> products = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}
