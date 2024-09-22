package org.styd.store.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.styd.store.entity.Order;
import org.styd.store.entity.OrderItem;
import org.styd.store.entity.Product;
import org.styd.store.entity.User;
import org.styd.store.repository.OrderItemRepository;
import org.styd.store.repository.OrderRepository;
import org.styd.store.repository.ProductRepository;
import org.styd.store.repository.UserRepository;
import org.styd.store.securingweb.CustomUserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/orders")
    public String viewOrders(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
        User user = currentUser.getUser();
        User.Role role = user.getRole();

        if(role == User.Role.ADMIN){
            List<Order> allOrders = orderRepository.findAll();
            model.addAttribute("orders", allOrders);
        } else if(role == User.Role.BUYER ){
            List<Order> buyerOrders = orderRepository.findByBuyer(user);
            model.addAttribute("orders", buyerOrders);
        }

        return "orders";
    }

    @GetMapping("/order/{orderId}")
    public String viewOrder(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable Long orderId, Model model) {

        Order order = orderRepository.findOrderById(orderId);

        // redirect to index if the order does not exist
        if (order == null) {
            return "index";
        }

        // redirect to index if the current user is neither the admin nor the buyer who owns the order
        Long buyerId = order.getBuyer().getId();
        boolean isBuyerEqualUser = checkBuyerEqualUser(currentUser, buyerId);
        if(!isBuyerEqualUser && currentUser.getUser().getRole() != User.Role.ADMIN){
            return "index";
        }

        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrder(order);

        // this list will contain product names in addition to order items
        List<Map<String, Object>> orderItemsWithNames = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            String productName =  productRepository.findProductById(orderItem.getProductId()).getName() ;
            // Creating a map to store the order item details along with product name
            Map<String, Object> orderItemWithName = new HashMap<>();
            orderItemWithName.put("orderItem", orderItem);
            orderItemWithName.put("productName", productName);
            orderItemsWithNames.add(orderItemWithName);
        }

        model.addAttribute("orderItemsWithNames", orderItemsWithNames);



        return "single-order";
    }

    @GetMapping("/seller/orderItems")
    public String viewOrderItems(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
        User user = currentUser.getUser();
        Long userId = user.getId();

        List<OrderItem> sellerOrderItems = new ArrayList<>();
        List<OrderItem> allOrdersItems = orderItemRepository.findAll();

        for (OrderItem orderItem : allOrdersItems) {
            Long productId = orderItem.getProductId();
            Product product = productRepository.findProductById(productId);
            Long sellerId = product.getSeller().getId();
            if(sellerId.equals(userId)){
                sellerOrderItems.add(orderItem);
            }
        }
        model.addAttribute("orderItems", sellerOrderItems);

        return "order-items";
    }

    // check if the authenticated user has the same id as sellerId in the URL
    // this will grant him the possibility to modify and delete that specific list of products
    private boolean checkBuyerEqualUser(@AuthenticationPrincipal CustomUserDetails currentUser, Long buyerId) {
        User user = currentUser.getUser();
        Long currentUserId = user.getId();
        return buyerId.equals(currentUserId);
    }
}