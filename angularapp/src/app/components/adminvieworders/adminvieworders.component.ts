import { Component, OnInit } from '@angular/core';
import { Orders } from 'src/app/models/orders.model';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-adminvieworders',
  templateUrl: './adminvieworders.component.html',
  styleUrls: ['./adminvieworders.component.css']
})
export class AdminviewordersComponent implements OnInit {

  orders: Orders[] = [];
  paginatedOrders: Orders[] = [];
  selectedUser: any = null; // To store the selected user's profile
  isLoading = false;

  showSuccessPopup = false; // ✅ Used for order deleted success dialog

  // Pagination properties
  pageSize = 5;
  currentPage = 1;
  totalPages: number[] = [];

  constructor(private readonly orderService: OrderService) {}

  ngOnInit(): void {
    this.fetchOrders();
  }

  fetchOrders(): void {
    this.isLoading = true;
    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders = data.sort((a, b) => b.orderId-a.orderId);
        this.setupPagination();
        this.isLoading = false;
      },
      error: () => {
        console.error('Error fetching orders');
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

  updateOrderStatus(orderId: number, status: string): void {
    const order = this.orders.find(o => o.orderId === orderId);
    if (order) {
      const updatedOrder: Orders = { ...order, orderStatus: status };
      this.isLoading = true;
      this.orderService.updateOrder(orderId, updatedOrder).subscribe({
        next: (updatedData) => {
          order.orderStatus = updatedData.orderStatus;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
          console.error('Error updating order status');
        },
      });
    }
  }

  deleteOrder(orderId: number): void {
    this.isLoading = true;
    this.orderService.deleteOrder(orderId).subscribe({
      next: () => {
        this.orders = this.orders.filter(order => order.orderId !== orderId);
        this.setupPagination(); // Refresh pagination after deletion
        this.isLoading = false;
        this.showSuccessPopup = true;
        this.fetchOrders();
      },
      error: () => {
        console.error('Error deleting order');
        this.isLoading = false;
        alert('Failed to delete order.');
      },
    });
  }

  closeSuccessPopup(): void {
    this.showSuccessPopup = false;
  }

  showUserProfile(userId: number): void {
    const order = this.orders.find(o => o.user.userId === userId);
    if (order) {
      this.selectedUser = order.user;
      console.log('Selected User:', this.selectedUser);
    } else {
      console.log('User not found for userId:', userId);
    }
  }

  closeUserProfile(): void {
    this.selectedUser = null;
  }
}