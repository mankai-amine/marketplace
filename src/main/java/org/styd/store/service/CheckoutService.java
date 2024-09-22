package org.styd.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.styd.store.entity.*;
import org.styd.store.exception.InsufficientStockException;
import org.styd.store.exception.PaymentFailureException;
import org.styd.store.repository.*;

import java.time.LocalDateTime;
import java.util.List;


// LOGIC FLOW:
// retrieve cartItems via buyerId
// verify that every cartItem has enough stock
// validate payment
// create a new order, and assign a buyer_id and orderDate to it
// for each cartItem, create an orderItem and populate it with orderId, productId, price and amount
// deduct the amount of each orderItem from stockAmount in the Products table in the database
// delete the cartItems related to buyerId from the database
// the checkout is approved in the Controller which redirects the buyer to the home page with a success message

@Service
public class CheckoutService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentService paymentService;


    // if at any time an exception is thrown, all database operations performed will be rolled back, as if they never happened
    @Transactional(rollbackFor = Exception.class)
    public void processCheckout(User buyer) throws InsufficientStockException, PaymentFailureException {

        // retrieve cartItems related to the buyer
        List<CartItem> cartItems = cartItemRepository.findByBuyer(buyer);

        // verify that every cartItem has enough stock
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if(cartItem.getAmount()>product.getStockAmount()){
                throw new InsufficientStockException("Insufficient stock");
            }
        }

        // validate payment
        boolean paymentSuccess = paymentService.processPayment();
        if (!paymentSuccess){
            throw new PaymentFailureException("Payment failed");
        }

        // create a new order, and assign a buyer_id, orderDate and shipment address to it
        Order order = new Order();
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setShipmentAddress(buyer.getAddress());
        Order savedOrder = orderRepository.save(order);
        double totalPrice=0;

        for (CartItem cartItem : cartItems) {
            // for each cartItem, create an orderItem and populate it with order, productId, price and amount
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(cartItem.getAmount());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);

            // deduct the amount of each orderItem from stockAmount (Products) in the database
            Product product = cartItem.getProduct();
            product.setStockAmount(product.getStockAmount() - cartItem.getAmount());
            productRepository.save(product);

            totalPrice += cartItem.getAmount()*cartItem.getProduct().getPrice();

            cartItemRepository.delete(cartItem);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }
}
