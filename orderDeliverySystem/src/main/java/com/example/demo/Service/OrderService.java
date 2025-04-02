package com.example.demo.Service;

import com.example.demo.Kafka.NotificationProducer;
//import com.example.demo.Kafka.StatusProducer;
import com.example.demo.Model.*;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Repository.OrderRepo;
import com.example.demo.Repository.PaymentRepo;
import com.example.demo.Service.CartService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.metrics.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PaymentRepo paymentRepo;



    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    public void processPaymentAndMoveToOrder(long cartPaymentId) {
        log.info("Processing payment and moving to order for cartPaymentId: {}", cartPaymentId);

        Optional<Payment> paymentOptional = paymentRepo.findByCartPaymentId(cartPaymentId);

        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            log.info("Found payment: {}", payment);

            if (payment.isPayment()) {
                Optional<Cart> cartOptional = cartRepo.findByCartId(cartPaymentId);

                if (cartOptional.isPresent()) {
                    Cart cart = cartOptional.get();
                    log.info("Found cart: {}", cart);
                    List<Cart> listCart = new ArrayList<>();
                    log.error("listCart not found");
                    listCart.add(cart);
                    Order order = new Order();
                    order.setOrderCart(listCart);
                    log.info("Order created and cart deleted successfully: {}",order);
                    orderRepo.save(order);
                    log.info("Order created and cart deleted successfully");

                    cartRepo.deleteById(cartPaymentId);
                } else {
                    log.error("Cart not found with ID: {}", cartPaymentId);
                    throw new RuntimeException("Cart not found with ID: " + cartPaymentId);
                }
            } else {
                log.error("Payment not successful for ID: {}", cartPaymentId);
                throw new RuntimeException("Payment not successful for ID: " + cartPaymentId);
            }
        } else {
            log.error("Payment not found with Cart Payment ID: {}", cartPaymentId);
            throw new RuntimeException("Payment not found with Cart Payment ID: " + cartPaymentId);
        }
    }

    public Order getOrderById(long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getAllOrders(Long id) {
        if (id == null) {
            log.info("all orders in order repo: "+ orderRepo.findAll().toString());
            return orderRepo.findAll();
        } else {
            return orderRepo.findByOrderId(id);
        }
    }

    //kafka active status

//    private StatusProducer statusProducer;
//
//    public OrderService(StatusProducer statusProducer) {
//        this.statusProducer = statusProducer;
//    }
//
//    public void activeStatus(Status status){
//        List<Order> orders = orderRepo.findAll();
//        log.info("Orders found: {}", orders);
//        Status newStatus = new Status();
//        statusProducer.activateStatus(newStatus);
//
//        log.info("Status activated");
//
//
////        notificationProducer.sendMessage("your order has been: "+ newStatus.toString() +" Order Details "+ status.getOrder().toString());
////        log.info("notification topic has been produced: {}", newStatus);
//    }
//


    // code for status

}