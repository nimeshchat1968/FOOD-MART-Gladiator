package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.examly.springapp.exception.InvalidInputException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.UserNotFoundException;
import com.examly.springapp.model.Food;
import com.examly.springapp.model.Orders;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.FoodRepo;
import com.examly.springapp.repository.OrderRepo;
import com.examly.springapp.repository.UserRepo;

/**
 * This class implements the OrderService interface and provides
 * the business logic for handling order-related operations.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final FoodRepo foodRepo;

    /**
     * Constructor-based dependency injection for OrderRepo, UserRepo, and FoodRepo.
     */
    public OrderServiceImpl(OrderRepo orderRepo, UserRepo userRepo, FoodRepo foodRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.foodRepo = foodRepo;
    }

    /**
     * Adds a new order.
     * Validates the order status, quantity, and date before saving.
     * Fetches and sets the associated user and food item.
     * Logs the addition process and returns the saved order.
     */
    @Override
    public Orders addOrder(Orders orders) {
        logger.info("Adding order: {}", orders);
        if (orders == null || orders.getOrderStatus() == null || orders.getOrderStatus().isEmpty()) {
            logger.error("Invalid Order status: {}", orders);
            throw new InvalidInputException("Order status cannot be null or empty.");
        }

        // Fetch and set User 
        User user = userRepo.findById(orders.getUser().getUserId()).orElse(null);
        if (user == null) {
            logger.error("User not found: {}", orders.getUser().getUserId());
            throw new UserNotFoundException("User not found");
        }
        orders.setUser(user);

        // Fetch and set Food
        Food food = foodRepo.findById(orders.getFood().getFoodId()).orElse(null);
        if (food == null) {
            logger.error("Food not found: {}", orders.getFood().getFoodId());
            throw new ResourceNotFoundException("Food not found"); 
        }
        if (food.getStockQuantity() < orders.getQuantity()) {
            if (food.getStockQuantity() == 0) {
                logger.error("Food out of stock: {}", food.getFoodName());
                throw new ResourceNotFoundException(food.getFoodName() + " Out of Stock!!");
            }
            logger.error("Insufficient stock for {}: Available Quantity is {}", food.getFoodName(), food.getStockQuantity());
            throw new ResourceNotFoundException("Available Quantity of " + food.getFoodName() + " is " + food.getStockQuantity());
        }
        orders.setFood(food);

        orders.setOrderStatus("Pending");
        orders.setOrderDate(LocalDate.now());
        food.setStockQuantity(food.getStockQuantity() - orders.getQuantity());
        Orders savedOrder = orderRepo.save(orders);
        logger.info("Order added successfully: {}", savedOrder);
        return savedOrder;
    }

    /**
     * Retrieves an order by ID.
     * Logs the retrieval process and returns the order if found.
     * Throws ResourceNotFoundException if the order is not found.
     */
    @Override
    public Orders getOrderById(int orderId) {
        logger.info("Fetching order by ID: {}", orderId);
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            logger.error("Order not found with Id: {}", orderId);
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }
        logger.info("Order found: {}", order);
        return order;
    }

    /**
     * Retrieves all orders.
     * Logs the retrieval process and returns the list of orders.
     * Throws ResourceNotFoundException if no orders are found.
     */
    @Override
    public List<Orders> getAllOrders() {
        logger.info("Fetching all orders");
        List<Orders> orders = orderRepo.findAll();
        if (orders.isEmpty()) {
            logger.error("No orders available");
            throw new ResourceNotFoundException("No orders available.");
        }
        logger.info("Orders found: {}", orders);
        return orders;
    }

    /**
     * Updates an order by ID.
     * Validates the order status before updating.
     * Logs the update process and returns the updated order.
     * Throws ResourceNotFoundException if the order is not found.
     */
    @Override
    public Orders updateOrder(int orderId, Orders orderDetails) {
        logger.info("Updating order with ID: {}", orderId);
        Orders existingOrder = orderRepo.findById(orderId).orElse(null);
        if (existingOrder == null) {
            logger.error("Order not found with orderId: {}", orderId);
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }
        if (orderDetails == null || orderDetails.getOrderStatus() == null || orderDetails.getOrderStatus().isEmpty()) {
            logger.error("Invalid Order Status: {}", orderDetails);
            throw new InvalidInputException("Order status cannot be null or empty.");
        }
        orderDetails.setOrderId(orderId);
        Orders updatedOrder = orderRepo.save(orderDetails);
        logger.info("Order updated successfully: {}", updatedOrder);
        return updatedOrder;
    }

    /**
     * Deletes an order by ID.
     * Logs the deletion process and returns true if deletion is successful.
     * Throws ResourceNotFoundException if the order is not found.
     */
    @Override
    public boolean deleteOrder(int orderId) {
        logger.info("Deleting order with ID: {}", orderId);
    
        Orders order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            logger.error("Order not found with ID: {}", orderId);
            throw new ResourceNotFoundException("Order not found with ID: " + orderId);
        }

        // Restore stock quantity
        Food food = order.getFood();
        if (food != null) {
            food.setStockQuantity(food.getStockQuantity() + order.getQuantity());
            foodRepo.save(food); // Save updated stock quantity
            logger.info("Restored stock quantity for {}: {}", food.getFoodName(), food.getStockQuantity());
        }

        orderRepo.deleteById(orderId);
        logger.info("Order deleted successfully with ID: {}", orderId);
        return true;
    }

    /**
     * Retrieves orders by user ID.
     * Logs the retrieval process and returns the list of orders.
     * Throws ResourceNotFoundException if no orders are found for the user ID.
     */
    @Override
    public List<Orders> getOrdersByUserId(int userId) {
        logger.info("Fetching orders by User ID: {}", userId);
        User existingUser = userRepo.findById(userId).orElse(null);
        if (existingUser == null) {
            logger.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        List<Orders> userOrders = orderRepo.findOrdersByUserId(userId);
        if (userOrders.isEmpty()) {
            logger.error("No orders found for User ID: {}", userId);
            throw new ResourceNotFoundException("No orders found for User ID: " + userId);
        }
        logger.info("Orders found for User ID {}: {}", userId, userOrders);
        return userOrders;   
    }

    @Override
    public Orders createOrder(Orders orders) {
        logger.info("Adding order: {}", orders);
        if (orders == null || orders.getOrderStatus() == null || orders.getOrderStatus().isEmpty()) {
            logger.error("Invalid order status: {}", orders);
            throw new InvalidInputException("Order status cannot be null or empty.");
        }

        // Fetch and set User 
        User user = userRepo.findById(orders.getUser().getUserId()).orElse(null);
        if (user == null) {
            logger.error("User not found: {}", orders.getUser().getUserId());
            throw new UserNotFoundException("User not found");
        }
        orders.setUser(user);

        // Fetch and set Food
        Food food = foodRepo.findById(orders.getFood().getFoodId()).orElse(null);
        if (food == null) {
            logger.error("Food not found: {}", orders.getFood().getFoodId());
            throw new ResourceNotFoundException("Food not found"); 
        }
        if (food.getStockQuantity() < orders.getQuantity()) {
            if (food.getStockQuantity() == 0) {
                logger.error("Food out of stock: {}", food.getFoodName());
                throw new ResourceNotFoundException(food.getFoodName() + " Out of Stock!!");
            }
            logger.error("Insufficient stock for {}: Available Quantity is {}", food.getFoodName(), food.getStockQuantity());
            throw new ResourceNotFoundException("Available Quantity of " + food.getFoodName() + " is " + food.getStockQuantity());
        }
        orders.setFood(food);

        // Set default Order Status
        orders.setOrderStatus("Pending");
        orders.setOrderDate(LocalDate.now());
        orders.setTotalAmount(food.getPrice()*orders.getQuantity());
        food.setStockQuantity(food.getStockQuantity() - orders.getQuantity());
        Orders savedOrder = orderRepo.save(orders);
        logger.info("Order added successfully: {}", savedOrder);
        return savedOrder;
    }

}