package com.examly.springapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

/**
 * This class is a REST controller for handling feedback-related requests.
 * It provides endpoints for creating, retrieving, and deleting feedback.
 */
@RestController
@RequestMapping("api/feedback")
@Tag(name = "Feedback", description = "Endpoints for managing feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    /**
     * Constructor-based dependency injection for FeedbackService.
     */
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Endpoint for creating feedback.
     * feedback the feedback object to be created
     * return a ResponseEntity containing the created Feedback and HTTP status 201
     */

    @Operation(summary = "Create a new feedback", description = "Allows a user to create a new feedback entry.")
    @PostMapping
    @RolesAllowed("USER")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback createFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.status(201).body(createFeedback);
    }

    /**
     * Endpoint for retrieving feedback by ID.
     * id the ID of the feedback to be retrieved
     * return a ResponseEntity containing the Feedback and HTTP status 200
     */
    @Operation(summary = "Get feedback by ID", description = "Allows an admin to retrieve feedback details by its ID.")
    @GetMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
        Feedback getFeedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.status(200).body(getFeedback);
    }

    /**
     * Endpoint for retrieving all feedbacks.
     * return a ResponseEntity containing the list of all feedbacks and HTTP status 200
     */
    @Operation(summary = "Get all feedbacks", description = "Allows an admin to retrieve all feedback entries.")
    @GetMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.status(200).body(feedbacks);
    }

    /**
     * Endpoint for retrieving feedbacks by user ID.
     * userId the ID of the user whose feedbacks are to be retrieved
     * return a ResponseEntity containing the list of feedbacks and HTTP status 200
     */
    @Operation(summary = "Get feedbacks by user ID", description = "Allows a user to retrieve all feedback entries they have created.")
    @GetMapping("/user/{userId}")
    @RolesAllowed("USER")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@PathVariable int userId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByUserId(userId);
        return ResponseEntity.status(200).body(feedbacks);
    }

    /**
     * Endpoint for deleting feedback by ID.
     * id the ID of the feedback to be deleted
     * return a ResponseEntity containing a boolean indicating the deletion status and HTTP status 200
     */
    @Operation(summary = "Delete feedback by ID", description = "Allows an admin or user to delete a feedback entry by its ID.")
    @DeleteMapping("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Boolean> deleteFeedback(@PathVariable int id) {
        boolean isDeleted = feedbackService.deleteFeedback(id);
        return ResponseEntity.status(200).body(isDeleted);
    }
}
