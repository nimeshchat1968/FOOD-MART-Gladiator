package com.examly.springapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Food;
import com.examly.springapp.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

/**
 * This class is a REST controller for handling food-related requests.
 * It provides endpoints for creating, retrieving, updating, and deleting food items.
 */
@RestController
@RequestMapping("api/food")
@Tag(name = "Food", description = "Endpoints for managing food items")
public class FoodController {

    private final FoodService foodService;

    /**
     * Constructor-based dependency injection for FoodService.
     */
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    /**
     * Endpoint for adding a new food item.
     * Creates a new food item and returns the created food item with HTTP status 201.
     */
    
    @Operation(summary = "Add a new food item", description = "Allows an admin to add a new food item to the inventory.")
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Food> addFood(@RequestBody Food food) {
        Food createdFood = foodService.addFood(food);
        return ResponseEntity.status(201).body(createdFood);
    }

    /**
     * Endpoint for retrieving a food item by ID.
     * Retrieves a food item by its ID and returns it with HTTP status 200.
     */

    @Operation(summary = "Get food item by ID", description = "Allows an admin or user to retrieve a food item by its ID.")
    @GetMapping("/{foodId}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Food> getFoodById(@PathVariable int foodId) {
        Food food = foodService.getFoodById(foodId);
        return ResponseEntity.status(200).body(food);
    }

    /**
     * Endpoint for retrieving all food items.
     * Retrieves all food items and returns them with HTTP status 200.
     */

    @Operation(summary = "Get all food items", description = "Allows an admin to retrieve all food items in the inventory.")

    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<Food>> getAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        return ResponseEntity.status(200).body(foods);
    }

    /**
     * Endpoint for updating a food item by ID.
     * Updates a food item by its ID and returns the updated food item with HTTP status 200.
     */

    @Operation(summary = "Update food item by ID", description = "Allows an admin to update the details of a food item by its ID.")
    @PutMapping("/{foodId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Food> updateFood(@PathVariable int foodId, @RequestBody Food foodDetails) {
        Food updatedFood = foodService.updateFood(foodId, foodDetails);
        return ResponseEntity.status(200).body(updatedFood);
    }

    /**
     * Endpoint for deleting a food item by ID.
     * Deletes a food item by its ID and returns a boolean indicating the deletion status with HTTP status 200.
     */

    @Operation(summary = "Delete food item by ID", description = "Allows an admin to delete a food item by its ID.")
    @DeleteMapping("/{foodId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Boolean> deleteFood(@PathVariable int foodId) {
        boolean isDeleted = foodService.deleteFood(foodId);
        return ResponseEntity.status(200).body(isDeleted);
    }

}