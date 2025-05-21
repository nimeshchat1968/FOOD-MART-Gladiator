import { Component, OnInit } from '@angular/core';
import { Food } from 'src/app/models/food.model'; // Food model
import { Feedback } from 'src/app/models/feedback.model'; // Feedback model
import { FoodService } from 'src/app/services/food.service';
import { FeedbackService } from 'src/app/services/feedback.service';
import { User } from 'src/app/models/user.model';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-useraddfeedback',
  templateUrl: './useraddfeedback.component.html',
  styleUrls: ['./useraddfeedback.component.css']
})
export class UseraddfeedbackComponent implements OnInit {
  foodList: Food[] = []; // Food list for dropdown
  foodId:any
  feedback: Feedback = {
    feedbackText: '',
    rating: 0,
    food: {foodId:0} as Food,
    user: { userId: 0 } as User, // Initialize the nested user object
  }; // Feedback object bound to the form
  successMessage: string = '';
  errorMessage: string = '';
  isLoading = true;

  constructor(private readonly foodService: FoodService, private readonly feedbackService: FeedbackService, private readonly router:Router) {}

  ngOnInit(): void {
    this.isLoading = true; // Show spinner
    setTimeout(() => {
      this.isLoading = false; // Hide spinner after delay
    }, 1500);

    this.loadFoodList();
    const userId = Number(localStorage.getItem('userId')); // Retrieve userId from localStorage
    if (!isNaN(userId) && userId > 0) {
      this.feedback.user.userId = userId; // Assign userId to nested user object
    } else {
      this.errorMessage = 'User not logged in. Please log in to submit feedback.';
    }
  }
  

  loadFoodList(): void {
    this.foodService.getAllFoods().subscribe({
      next: (data) => {
        this.foodList = data;
      },
      error: () => {
        this.errorMessage = 'Failed to load food list.';
      }
    });
  }

  onSubmit(feedbackForm:NgForm): void {
    if (this.foodId && this.feedback.feedbackText.length >= 10 && this.feedback.rating >= 1 && this.feedback.rating <= 5) {
      this.feedback.date = new Date(); // Add current date
      this.feedback.food.foodId=this.foodId
      this.feedbackService.sendFeedback(this.feedback).subscribe(
         () => {
          this.successMessage = 'Thank you for your feedback!';
           this.feedback = { feedbackText: '', rating: 0, user: { userId: 0} }; // Reset form
         //feedbackForm.reset();
          this.router.navigate(['/userViewFeedback'])
        },
         (err) => {
          console.error('Error submitting feedback:', err);
          this.errorMessage = 'Failed to submit feedback. Please try again later.';
        }
      );
    } else {
      this.errorMessage = 'Please fill out all required fields correctly!';
    }
  }
  
setRating(rating: number): void {
   this.feedback.rating = rating;
 }
  
  
}