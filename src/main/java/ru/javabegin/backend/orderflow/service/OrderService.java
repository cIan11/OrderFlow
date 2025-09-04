package ru.javabegin.backend.orderflow.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.repository.OrderRepository;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
}
