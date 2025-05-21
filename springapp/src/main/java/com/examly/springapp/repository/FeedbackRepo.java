package com.examly.springapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.examly.springapp.model.Feedback;


public interface FeedbackRepo extends JpaRepository<Feedback,Integer>{
    @Query("select f from Feedback f where f.user.userId=?1")
    public List<Feedback> findByUserId(int userId);

}
