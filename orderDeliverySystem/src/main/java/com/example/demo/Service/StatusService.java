package com.example.demo.Service;


import com.example.demo.Events.OrderEvent;
import com.example.demo.Kafka.NotificationProducer;
import com.example.demo.Model.Cart;
import com.example.demo.Model.Order;
import com.example.demo.Model.Shopping;
import com.example.demo.Model.Status;
import com.example.demo.Repository.OrderRepo;
import com.example.demo.Repository.StatusRepository;

import com.example.demo.States.OrderStatus;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private StateMachine<OrderStatus, OrderEvent> stateMachine;

//    private KafkaTemplate<OrderStatus, String> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(StatusService.class);

    // Fetch all statuses, create default statuses if none are present
    public List<Status> getAllStatuses() {
        List<Order> orders = orderRepository.findAll();
        if (statusRepository.findAll().isEmpty()) {


            log.info("Orders available for status: "+ orders.toString());
            List<Status> statuses = new ArrayList<>();
            log.info("Orders fetched: {}", orders);

            for (Order order : orders) {
                Status status = new Status();
                status.setOrder(order);
                // Save status and avoid violating primary key constraints
                Status savedStatus = statusRepository.save(status);
                statuses.add(savedStatus);
                log.info("Status created: {}", savedStatus);


            }
            return statuses;
        } else {
//            statusRepository.deleteAll();
            for(Order order: orders){
                Optional<Status> statusAvailable = statusRepository.findByOrder(order);
                if(statusAvailable.isPresent()){
                    continue;
                }else{
                    Status status = new Status();;
                    status.setOrder(order);
                    statusRepository.save(status);
                }

            }
            return statusRepository.findAll();
        }
    }
    private NotificationProducer notificationProducer;

    public StatusService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }
    // Update the status of a specific order using a state machine
    public Status updateOrderStatus(Long statusId, OrderEvent event) {
        log.info("Received event: {}", event);

        // Fetch the existing status entity
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found for this ID"));

        log.info("Current status: {}", status);

        // Start the state machine and send the event
        stateMachine.start();
        stateMachine.sendEvent(event);

        // Get the new state from the state machine
        OrderStatus newStatus = stateMachine.getState().getId();
        log.info("New state after event: {}", newStatus);

        // Update the status entity
        status.setOrderStatus(newStatus);
        //************************** kafka ***************

        notificationProducer.sendMessage("your order has been: "+ newStatus.toString() +" Order Details "+ status.getOrder().toString());
        log.info("notification topic has been produced: {}", newStatus);




        //******************************
        // Save the updated status entity
        Status updatedStatus = statusRepository.save(status);
        log.info("Updated status: {}", updatedStatus);

        return updatedStatus;
    }




    //kafka integration





}











//
//@Service
//public class StatusService {
//
//    @Autowired
//    private StatusRepository statusRepository;
//
//    @Autowired
//    private OrderRepo orderRepository; // Assuming you already have this repository
//
////    public Cart addShoppingProductsToCart(Cart cart) {
////        List<Shopping> shoppingProducts = shoppingRepository.findByShopNotShopTrue();
////        cart.setShoppingProducts(shoppingProducts);
////        cartRepo.save(cart);
////        return cart;
////    }
//
//    @Autowired
//    private StateMachine<OrderStatus, OrderEvent> stateMachine;
//
//    private static final Logger log = LoggerFactory.getLogger(StatusService.class);
//    // Fetch all orders and create default statuses if not already present
////    public List<Status> getAllStatuses() {
////
////        List<Order> orders = orderRepository.findAll();
////        List<Status> statuses = new ArrayList<>();
////        log.info(orders.toString());
////        for (Order order : orders) {
////            Status status = new Status();
////            status.setOrder(order);
////            statusRepository.save(status);
////            log.info(status.toString());
////            statuses.add(status);
////        }
////        return statuses;
////    }
//
//    public List<Status> getAllStatuses() {
//        if(statusRepository.findAll().isEmpty()){
//            List<Order> orders = orderRepository.findAll();
//
//
//            List<Status> statuses = new ArrayList<>();
//            log.info(orders.toString());
//            for (Order order : orders) {
//                Status status = new Status();
//                status.setOrder(order);
//                // Ensure that `statusRepository.save(status)` does not violate primary key constraints
//                statuses.add(statusRepository.save(status));
////            statusRepository.save(status);
//                log.info(status.toString());
//            }
//            return statuses;
//
//        }else{
//            return statusRepository.findAll();
//        }
//
//
//    }
//
//    // Update the status of a specific order
////    public Status updateOrderStatus(Long orderId, OrderStatus newStatus) {
////
////        Order order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
////
////        Status status = statusRepository.findByOrder(order)
////                .orElseThrow(() -> new EntityNotFoundException("Status not found for this order"));
////
////        status.setOrderStatus(newStatus);
////        return statusRepository.save(status);
////    }
//
//    //modifications
//
//    public Status updateOrderStatus(Long statusId, OrderEvent event) {
//        log.info("Received event: {}", event);
//
//        // Fetch the existing status entity
//        Status status = statusRepository.findById(statusId)
//                .orElseThrow(() -> new EntityNotFoundException("Status not found for this order"));
//
//        log.info("Current status: {}", status);
//
//        // Start the state machine and send the event
//        stateMachine.start();
//        stateMachine.sendEvent(event);
//        log.info("State machine transition complete");
//
//        // Get the new state from the state machine
//        OrderStatus newStatus = stateMachine.getState().getId();
//
//        // Update the status entity
//        status.setOrderStatus(newStatus);
//
//        // Save the updated status entity
//        return status;
//    }
//
//
//}
