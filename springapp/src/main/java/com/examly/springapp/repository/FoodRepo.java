package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Food;

public interface FoodRepo extends JpaRepository<Food,Integer>{

}
