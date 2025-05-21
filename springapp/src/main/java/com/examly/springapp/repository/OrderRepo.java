package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.examly.springapp.model.Orders;

public interface OrderRepo extends JpaRepository<Orders,Integer> {
    @Query("select orders from Orders orders where orders.user.userId=?1")
    List<Orders> findOrdersByUserId(int userId);

}
