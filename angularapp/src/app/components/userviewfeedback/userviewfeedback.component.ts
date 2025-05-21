import { Component, OnInit } from '@angular/core';
import { Feedback } from 'src/app/models/feedback.model'; // Assume a Feedback model exists
import { FeedbackService } from 'src/app/services/feedback.service'; // Service to fetch feedback

@Component({
  selector: 'app-userviewfeedback',
  templateUrl: './userviewfeedback.component.html',
  styleUrls: ['./userviewfeedback.component.css']
})
export class UserviewfeedbackComponent implements OnInit {
  feedbackList: Feedback[] = [];
  paginatedFeedbacks: Feedback[] = [];
  errorMessage: string = '';
  userId: number = 0; 
  isLoading = true;
  showDeleteConfirmation = false;
  deleteFeedbackId: number | null = null; // Store feedback id to delete
  // ✅ Pagination properties
  pageSize = 4; // Number of feedback items per page
  currentPage = 1;
  totalPages: number[] = [];

  constructor(private feedbackService: FeedbackService) { }

  ngOnInit(): void {
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      this.userId = parseInt(storedUserId, 10);
      this.loadFeedback(this.userId);
    } else {
      this.errorMessage = 'User ID not found. Cannot load feedback.';
      this.isLoading = false; 
    }
  }

  loadFeedback(userId: number): void {
    this.isLoading = true;
    this.feedbackService.getAllFeedbacksByUserId(userId).subscribe({
      next: (data) => {
        this.feedbackList = data.sort((a, b) => b.feedbackId-a.feedbackId);
        this.feedbackList = data;
        this.setupPagination();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load feedback';
        this.isLoading = false;
      }
    });
  }

  // Trigger the confirmation popup
  confirmDelete(feedbackId: number): void {
    this.deleteFeedbackId = feedbackId;
    this.showDeleteConfirmation = true; // Show the confirmation popup
  }

  // Cancel deletion
  cancelDelete(): void {
    this.showDeleteConfirmation = false; // Hide the confirmation popup
    this.deleteFeedbackId = null; // Reset the feedback ID to delete
  }

  // Perform delete operation
  setupPagination(): void {
    this.totalPages = Array(Math.ceil(this.feedbackList.length / this.pageSize))
      .fill(0)
      .map((_, i) => i + 1);
    this.updatePaginatedFeedback();
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages.length) {
      this.currentPage = page;
      this.updatePaginatedFeedback();
    }
  }

  updatePaginatedFeedback(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    this.paginatedFeedbacks = this.feedbackList.slice(startIndex, startIndex + this.pageSize);
  }


  deleteFeedback(feedbackId: number): void {
    if (feedbackId !== null) {
      this.feedbackService.deleteFeedback(feedbackId).subscribe(
        () => {
          this.feedbackList = this.feedbackList.filter(feedback => feedback.feedbackId !== feedbackId); // Remove the deleted feedback from the list
          this.showDeleteConfirmation = false; // Hide the confirmation popup
          this.deleteFeedbackId = null; // Reset the feedback ID
        },
        (error) => {
          console.error('Error deleting feedback', error);
          this.errorMessage = 'Failed to delete feedback.';
        }
      );
    }
  }
}