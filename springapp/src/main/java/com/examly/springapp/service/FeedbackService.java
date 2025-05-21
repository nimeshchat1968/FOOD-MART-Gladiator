package com.examly.springapp.service;
import java.util.List;
import com.examly.springapp.model.Feedback;


public interface FeedbackService {
    public Feedback createFeedback(Feedback feedback);
    public Feedback getFeedbackById(int id);
    public List<Feedback>getAllFeedbacks();
    public boolean deleteFeedback(int id);
    public List<Feedback> getFeedbacksByUserId(int userId);
}
