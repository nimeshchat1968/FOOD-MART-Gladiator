import { Component, OnInit } from '@angular/core';
import { Feedback } from 'src/app/models/feedback.model'; // Feedback model
import { FeedbackService } from 'src/app/services/feedback.service'; // Service to fetch feedback

@Component({
  selector: 'app-adminviewfeedback',
  templateUrl: './adminviewfeedback.component.html',
  styleUrls: ['./adminviewfeedback.component.css']
})
export class AdminviewfeedbackComponent implements OnInit {
  feedbackList: Feedback[] = [];
  paginatedFeedbacks: Feedback[] = [];
  errorMessage: string = '';
  isLoading = false;
  showDialog = false; // ✅ For feedback deleted popup
  showDeleteConfirmation = false; // ✅ For delete confirmation popup
  confirmFeedbackId: number = 0; // Store the ID of the feedback to delete
  // ✅ Pagination properties
  pageSize = 5; // Number of feedbacks per page
  currentPage = 1;
  totalPages: number[] = [];

  constructor(private readonly feedbackService: FeedbackService) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.loadFeedback();
  }

  loadFeedback(): void {
    this.feedbackService.getFeedbacks().subscribe({
      next: (data) => {
        // Sort feedbacks by date in descending order
        this.feedbackList = data.sort((a, b) => b.feedbackId-a.feedbackId);
        this.setupPagination();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load feedback';
        this.isLoading = false;
      }
    });
  }
  

  // Trigger delete confirmation dialog
  confirmDelete(feedbackId: number): void {
    this.confirmFeedbackId = feedbackId;
    this.showDeleteConfirmation = true; // Show confirmation dialog
  }

  // Cancel deletion and close the confirmation dialog
  cancelDelete(): void {
    this.showDeleteConfirmation = false;
  }

  // Perform deletion if confirmed
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
    this.isLoading = true;
    this.feedbackService.deleteFeedback(feedbackId).subscribe(
      () => {
        // Remove the feedback from the list without reloading the page
        this.feedbackList = this.feedbackList.filter(
          feedback => feedback.feedbackId !== feedbackId
        );
        this.isLoading = false;
        this.showDeleteConfirmation = false; // Close the confirmation dialog
        this.showDialog = true; // Show success popup
      },
      (error) => {
        console.error('Error deleting feedback', error);
        this.isLoading = false;
        this.errorMessage = 'Failed to delete feedback.';
        this.showDeleteConfirmation = false; // Close the confirmation dialog
      }
    );
  }

  // Close the success popup
  onDialogConfirm(): void {
    this.showDialog = false;
  }
}