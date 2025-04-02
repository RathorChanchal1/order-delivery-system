//package com.example.demo.Service;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class CartService {
//}

package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Order;
import com.example.demo.Model.Payment;
import com.example.demo.Model.Shopping;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Repository.OrderRepo;
import com.example.demo.Repository.ShoppingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ShoppingRepo shoppingRepository;

    public Cart addShoppingProductsToCart(Cart cart) {
        List<Shopping> shoppingProducts = shoppingRepository.findByShopNotShopTrue();
        cart.setShoppingProducts(shoppingProducts);
        cartRepo.save(cart);
        return cart;
    }


    //modifications
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);






//
    public void processPayment(Long cartId) {
        logger.info("Processing payment for cartId: {}", cartId);

        String paymentUrl = "http://localhost:8083/payment/payments/" + cartId;
        logger.info("Calling payment service at: {}", paymentUrl);

        Payment payment;
        try {
            payment = restTemplate.getForObject(paymentUrl, Payment.class);
            logger.info("Received payment response: {}", payment);
        } catch (Exception e) {
            logger.error("Error calling payment service", e);
            throw new RuntimeException("Error calling payment service", e);
        }

        if (payment != null && payment.isPayment()) {
            logger.info("Payment successful for cartId: {}", cartId);

            Optional<Cart> cart = cartRepo.findByCartId(cartId);
//            List<Cart> cart2 = cartRepo.findAll();
//                    .orElseThrow(() -> new RuntimeException("Cart not found"));
//            logger.info("Order Saved in OrderRepo1: {}", cart2);
            Order order = new Order();
            List<Cart> cartList = new ArrayList<>();
            cart.ifPresent(cartList::add);
            order.setOrderCart(cartList);
            orderRepo.save(order);
            List<Shopping> shoppingProducts = cart.get().getShoppingProducts();
            for(Shopping shopping : shoppingProducts) {
                shopping.setShopNotShop(false);
            }
            cartRepo.deleteById(cartId);
            logger.info("Cart Deleted: {}", cartId);
        } else {
            logger.error("Payment not successful or not found for cartId: {}", cartId);
            throw new RuntimeException("Payment not successful or not found");
        }
    }




}