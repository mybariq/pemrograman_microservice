package com.teknologiinformasi.restapi.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.teknologiinformasi.restapi.order.event.OrderCreatedEvent;
import com.teknologiinformasi.restapi.order.model.Order;
import com.teknologiinformasi.restapi.order.model.OrderResponse;
import com.teknologiinformasi.restapi.order.model.Produk;
import com.teknologiinformasi.restapi.order.service.OrderMessageProducer;
import com.teknologiinformasi.restapi.order.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

   @Autowired
   private OrderService orderService;

   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private OrderMessageProducer orderMessageProducer;

   // GET semua order
   @GetMapping
   public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
   }

   // GET order berdasarkan ID dan ambil detail order dari order Service
   @GetMapping("/{id}")
   public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        // Panggil Product Service untuk mendapatkan data produk terkait
        String productServiceUrl = "http://localhost:8081/api/produk/" + order.getProductId();
        Produk product = restTemplate.getForObject(productServiceUrl, Produk.class);


        OrderResponse response = new OrderResponse();
        response.setOrder(order);
        response.setProduct(product);
        return ResponseEntity.ok(response);
   }

   // POST buat order baru
   @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
      // Set informasi order
      order.setOrderDate(LocalDateTime.now());
      order.setOrderStatus("CREATED");
      Order createdOrder = orderService.createOrder(order);
    
      // Buat dan kirim event
      OrderCreatedEvent event = new OrderCreatedEvent(
          createdOrder.getId(),
          createdOrder.getProductId(),
          createdOrder.getQuantity(),
          createdOrder.getOrderDate(),
          createdOrder.getOrderStatus()
      );
      orderMessageProducer.sendOrderCreatedEvent(event);
    
      return ResponseEntity.ok(createdOrder);
  }

   // PUT update order
   @PutMapping("/{id}")
   public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
   }

   // DELETE order
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
   }
}
