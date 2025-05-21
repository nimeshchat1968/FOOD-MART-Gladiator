package com.examly.springapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.examly.springapp.exception.InvalidInputException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.model.Food;
import com.examly.springapp.repository.FoodRepo;

/**
 * This class implements the FoodService interface and provides
 * the business logic for handling food-related operations.
 */
@Service
public class FoodServiceImpl implements FoodService {

    private static final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);
    private final FoodRepo foodRepo;
    /**
     * Constructor-based dependency injection for FoodRepo.
     */
    public FoodServiceImpl(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }

    /**
     * Adds a new food item.
     * Validates the food name, price, and stock quantity before saving.
     * Logs the addition process and returns the saved food item.
     */
    @Override
    public Food addFood(Food food) {
        logger.info("Adding food: {}", food);
        if (food == null || food.getFoodName() == null || food.getFoodName().isEmpty()) {
            logger.error("Invalid food name: {}", food);
            throw new InvalidInputException("Food name cannot be null or empty.");
        }
        if (food.getPrice() <= 0) {
            logger.error("Invalid food price: {}", food.getPrice());
            throw new InvalidInputException("Price must be greater than zero.");
        }
        if (food.getStockQuantity() < 0) {
            logger.error("Invalid stock quantity: {}", food.getStockQuantity());
            throw new InvalidInputException("Stock quantity cannot be negative.");
        }
        Food savedFood = foodRepo.save(food);
        logger.info("Food added successfully: {}", savedFood);
        return savedFood;
    }

    /**
     * Retrieves all food items.
     * Logs the retrieval process and returns the list of food items.
     * Throws ResourceNotFoundException if no food items are found.
     */
    @Override
    public List<Food> getAllFoods() {
        logger.info("Fetching all foods");
        List<Food> foods = foodRepo.findAll();
        if (foods.isEmpty()) {
            logger.error("No foods available");
            throw new ResourceNotFoundException("No foods available.");
        }
        logger.info("Foods found: {}", foods);
        return foods;
    }

    /**
     * Retrieves a food item by ID.
     * Logs the retrieval process and returns the food item if found.
     * Throws ResourceNotFoundException if the food item is not found.
     */
    @Override
    public Food getFoodById(int foodId) {
        logger.info("Fetching food by ID: {}", foodId);
        Food food = foodRepo.findById(foodId).orElse(null);
        if (food == null) {
            throw new ResourceNotFoundException("Food not found with ID: " + foodId);
        }
        logger.info("Food found: {}", food);
        return food;
    }

    /**
     * Updates a food item by ID.
     * Validates the food name, price, and stock quantity before updating.
     * Logs the update process and returns the updated food item.
     * Throws ResourceNotFoundException if the food item is not found.
     */
    @Override
    public Food updateFood(int foodId, Food foodDetails) {
        logger.info("Updating food with ID: {}", foodId);
        if (foodDetails == null || foodDetails.getFoodName() == null || foodDetails.getFoodName().isEmpty()) {
            logger.error("Invalid food name: {}", foodDetails);
            throw new InvalidInputException("Food name cannot be null or empty.");
        }
        if (foodDetails.getPrice() <= 0) {
            logger.error("Invalid food price: {}", foodDetails.getPrice());
            throw new InvalidInputException("Price must be greater than zero.");
        }
        if (foodDetails.getStockQuantity() < 0) {
            logger.error("Invalid stock quantity: {}", foodDetails.getStockQuantity());
            throw new InvalidInputException("Stock quantity cannot be negative.");
        }

        Food existingFood = foodRepo.findById(foodId).orElse(null);
        if (existingFood == null) {
            throw new ResourceNotFoundException("Food not found with ID: " + foodId);
        }
        foodDetails.setFoodId(foodId);
        Food updatedFood = foodRepo.save(foodDetails);
        logger.info("Food updated successfully: {}", updatedFood);
        return updatedFood;
    }

    /**
     * Deletes a food item by ID.
     * Logs the deletion process and returns true if deletion is successful.
     * Throws ResourceNotFoundException if the food item is not found.
     */
    @Override
    public boolean deleteFood(int foodId) {
        logger.info("Deleting food with ID: {}", foodId);
        if (!foodRepo.existsById(foodId)) {
            throw new ResourceNotFoundException("Food not found with ID: " + foodId);
        }
        foodRepo.deleteById(foodId);
        logger.info("Food deleted successfully with ID: {}", foodId);
        return true;
    } 

}

