package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    // Create order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setOrderDate(new java.util.Date());
        order.setStatus("Pending");
        return orderRepository.save(order);
    }
    
    // Update order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setCustomerName(orderDetails.getCustomerName());
            updatedOrder.setProduct(orderDetails.getProduct());
            updatedOrder.setQuantity(orderDetails.getQuantity());
            updatedOrder.setPrice(orderDetails.getPrice());
            updatedOrder.setStatus(orderDetails.getStatus());
            return ResponseEntity.ok(orderRepository.save(updatedOrder));
        }
        return ResponseEntity.notFound().build();
    }
    
    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}