package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.Orders;

public interface OrderService {
    public Orders addOrder(Orders orders);
    public Orders getOrderById(int orderId);
    public List<Orders> getAllOrders();
    public Orders updateOrder(int orderId, Orders orderDetails);
    public boolean deleteOrder(int orderId);
    public List<Orders> getOrdersByUserId(int userId);
    public Orders createOrder(Orders orders);

}
