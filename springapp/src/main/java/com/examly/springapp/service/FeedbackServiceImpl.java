package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.InvalidInputException;
import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.Food;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.FeedbackRepo;
import com.examly.springapp.repository.FoodRepo;
import com.examly.springapp.repository.UserRepo;

/**
 * This class implements the FeedbackService interface and provides
 * the business logic for handling feedback-related operations.
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    private final FeedbackRepo feedbackRepo;
    /**
     * Constructor-based dependency injection for FeedbackRepo.
     */
    public FeedbackServiceImpl(FeedbackRepo feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }  
    @Autowired
    FoodRepo foodRepo;
    @Autowired
    UserRepo userRepo;


    /**
     * Creates a new feedback.
     * Validates the feedback content, rating, date, user, and food item before saving.
     * Logs the creation process and returns the saved feedback.
     */
    @Override
   public Feedback createFeedback(Feedback feedback) {
    logger.info("Creating feedback: {}", feedback);

    // Validate Feedback Content
    if (feedback == null || feedback.getFeedbackText() == null || feedback.getFeedbackText().isEmpty()) {
        logger.error("Invalid feedback content: {}", feedback);
        throw new InvalidInputException("Feedback content cannot be null or empty.");
    }

    // Validate Feedback Rating
    if (feedback.getRating() < 1 || feedback.getRating() > 5) {
        logger.error("Invalid feedback rating: {}", feedback.getRating());
        throw new InvalidInputException("Feedback rating must be between 1 and 5.");
    }

    // Validate Feedback Date
    if (feedback.getDate() != null && feedback.getDate().isAfter(LocalDate.now())) {
        logger.error("Invalid feedback date: {}", feedback.getDate());
        throw new InvalidInputException("Feedback date cannot be in the future.");
    }

    // Validate User Association
    if (feedback.getUser() == null || feedback.getUser().getUserId() == 0) {
        logger.error("Feedback without user: {}", feedback);
        throw new InvalidInputException("Feedback must have an associated user.");
    }

    // Validate Food Association
    if (feedback.getFood() == null || feedback.getFood().getFoodId() == 0) {
        logger.error("Feedback without food item: {}", feedback);
        throw new InvalidInputException("Feedback must be associated with a food item.");
    }

    // Fetch Food Entity
    Food food = foodRepo.findById(feedback.getFood().getFoodId())
        .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + feedback.getFood().getFoodId()));

    // Fetch User Entity
    User user = userRepo.findById(feedback.getUser().getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + feedback.getUser().getUserId()));

    // Set Food and User entities
    feedback.setFood(food);
    feedback.setUser(user);

    // Save Feedback Entity
    Feedback savedFeedback = feedbackRepo.save(feedback);
    logger.info("Feedback created successfully: {}", savedFeedback);
    return savedFeedback;
}

    /**
     * Retrieves feedback by ID.
     * Logs the retrieval process and returns the feedback if found.
     * Throws ResourceNotFoundException if feedback is not found.
     */
    @Override
    public Feedback getFeedbackById(int id) {
        logger.info("Fetching feedback by ID: {}", id);
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        if (feedback == null) {
            logger.error("Feedback not found with ID: {}", id);
            throw new ResourceNotFoundException("Feedback not found with ID: " + id);
        }
        logger.info("Feedback found: {}", feedback);
        return feedback;
    }

    /**
     * Retrieves all feedbacks.
     * Logs the retrieval process and returns the list of feedbacks.
     * Throws ResourceNotFoundException if no feedbacks are found.
     */
    @Override
    public List<Feedback> getAllFeedbacks() {
        logger.info("Fetching all feedbacks");
        List<Feedback> feedbacks = feedbackRepo.findAll();
        if (feedbacks.isEmpty()) {
            logger.error("No feedbacks available");
            throw new ResourceNotFoundException("No feedbacks available.");
        }
        logger.info("Feedbacks found: {}", feedbacks);
        return feedbacks;
    }

    /**
     * Deletes feedback by ID.
     * Logs the deletion process and returns true if deletion is successful.
     * Throws ResourceNotFoundException if feedback is not found.
     */
    @Override
    public boolean deleteFeedback(int id) {
        logger.info("Deleting feedback with ID: {}", id);
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        if (feedback == null) {
            logger.error("Feedback not found with ID: {}", id);
            throw new ResourceNotFoundException("Feedback not found with ID: " + id);
        }
        feedbackRepo.delete(feedback);
        logger.info("Feedback deleted successfully with ID: {}", id);
        return true;
    }

    /**
     * Retrieves feedbacks by user ID.
     * Logs the retrieval process and returns the list of feedbacks.
     * Throws ResourceNotFoundException if no feedbacks are found for the user ID.
     */
    @Override
    public List<Feedback> getFeedbacksByUserId(int userId) {
        logger.info("Fetching feedbacks by User ID: {}", userId);
        List<Feedback> feedbacksList = feedbackRepo.findByUserId(userId);
        if (feedbacksList == null || feedbacksList.isEmpty()) {
            logger.error("No feedbacks found for User ID: {}", userId);
            throw new ResourceNotFoundException("No feedbacks found for User ID: " + userId);
        }
        logger.info("Feedbacks found for User ID {}: {}", userId, feedbacksList);
        return feedbacksList;
    }
}
