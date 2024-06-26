package org.example.order.application.service;

import org.example.order.application.dto.CreateOrderRequest;
import org.example.order.application.port.OrderPort;
import org.example.order.domain.Order;
import org.example.product.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class HxOrderService {

    private final OrderPort orderPort;

    public HxOrderService(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        final Product product = orderPort.getProductById(request.getProductId());

        final Order order = new Order(product, request.getQuantity());

        orderPort.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
