package com.example.demo.Service;

//import com.example.demo.KafkaProducers.PaymentProducer;
import com.example.demo.Model.Payment;
import com.example.demo.Repository.PaymentRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    public Optional<Payment> getPaymentById(long paymentId){
        return paymentRepo.findByCartPaymentId(paymentId);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepo.save(payment);
    }


//    @Transactional
//    public void processPayment(long cartPaymentId) {
//        Optional<Payment> paymentOptional = paymentRepository.findByCartPaymentId(cartPaymentId);
//
//        if (paymentOptional.isPresent()) {
//            Payment payment = paymentOptional.get();
//
//            if (payment.isPayment()) {
//                Optional<Cart> cartOptional = cartRepository.findById(cartPaymentId);
//
//                if (cartOptional.isPresent()) {
//                    Cart cart = cartOptional.get();
//
//                    Order order = new Order();
//                    order.setOrderCart(List.of(cart));
//                    orderRepository.save(order);
//
//                    cartRepository.deleteByCartId(cartPaymentId);
//                }
//            }
//        }
//    }


}



//    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
//    //kafka modifications
//    @Autowired
//    private PaymentProducer paymentProducer;
//    public void processPayment(Payment payment){
//        logger.info("kafka process Payment called from paymentService: {}", payment);
//        String event = STR."Payment successful for cartId: \{payment.getPaymentId()}";
//        paymentProducer.sendMessage(payment);
//    }


//}


