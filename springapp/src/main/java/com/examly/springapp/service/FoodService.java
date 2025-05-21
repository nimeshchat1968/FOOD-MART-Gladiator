package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.Food;

public interface FoodService {
    public Food addFood(Food food);
    public List<Food> getAllFoods();
    public Food getFoodById(int foodId);
    public Food updateFood(int foodId, Food foodDetails);
    public boolean deleteFood(int foodId);

}
