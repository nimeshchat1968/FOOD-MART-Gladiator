import { Component, OnInit } from '@angular/core';
import { Orders } from 'src/app/models/orders.model';
import { OrderService } from 'src/app/services/order.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-uservieworders',
  templateUrl: './uservieworders.component.html',
  styleUrls: ['./uservieworders.component.css']
})
export class UserviewordersComponent implements OnInit {
  orders: Orders[] = [];
  userId: number = 0; 
  errorMessage: string = '';
  showConfirmation = false;
  orderToDelete: Orders | null = null;
  isLoading = true;
  pageSize = 5;
  currentPage = 1;
  paginatedOrders: Orders[] = [];
  totalPages: number[] = [];
  constructor(private orderService: OrderService, private router: Router) {}

  ngOnInit(): void {
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      this.userId = parseInt(storedUserId);
      this.loadOrders();
    } else {
      this.errorMessage = 'User ID not found. Cannot load orders.';
      this.isLoading = false; // Hide spinner if no user ID
    }
  }

  loadOrders(): void {
    this.isLoading = true;
    this.orderService.getAllOrdersByUserId(this.userId).subscribe({
      next: (data) => {
        this.orders = data.sort((a, b) => b.orderId-a.orderId);
        this.orders = data;
        this.setupPagination();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load order history';
        this.isLoading = false;
      },
    });
  }
  setupPagination(): void {
    this.totalPages = Array(Math.ceil(this.orders.length / this.pageSize))
      .fill(0)
      .map((_, i) => i + 1);
    this.updatePaginatedOrders();
  }
  
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages.length) {
      this.currentPage = page;
      this.updatePaginatedOrders();
    }
  }
  
  updatePaginatedOrders(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    this.paginatedOrders = this.orders.slice(startIndex, startIndex + this.pageSize);
  }

  confirmDelete(order: Orders): void {
    this.orderToDelete = order;
    this.showConfirmation = true;
  }

  deleteOrder(): void {
    if (this.orderToDelete) {
      this.orderService.deleteOrder(this.orderToDelete.orderId).subscribe({
        next: () => {
          this.orders = this.orders.filter(order => order.orderId !== this.orderToDelete?.orderId);
          this.showConfirmation = false;
          this.orderToDelete = null;
          this.loadOrders();
        },
        error: () => {
          this.errorMessage = 'Failed to delete order';
        },
      });
    }
  }

  cancelDelete(): void {
    this.showConfirmation = false;
    this.orderToDelete = null;
  }

  refreshPage(): void {
    console.log('hi');
    // this.router.navigate(['/userViewOrders']);
    this.loadOrders();
  }
  showAlert(): void {
    alert('Order is already on the way, so it cannot be canceled.');
  }
  
  redirectToFeedback(): void {
    this.router.navigate(['/userAddFeedback']);
  }
showPopup = false;

closePopup(): void {
  this.showPopup = false;
}
}
