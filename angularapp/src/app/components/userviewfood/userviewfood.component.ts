import { Component, OnInit } from '@angular/core';
import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food.service';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.model';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-userviewfood',
  templateUrl: './userviewfood.component.html',
  styleUrls: ['./userviewfood.component.css']
})
export class UserviewfoodComponent implements OnInit {
  isLoading = true; 
  foods: Food[] = []; 
  selectedFood: Food | null = null; 
  showOrderPopup = false; 
  orderQuantity = 1; 
  totalAmount = 0; 
  orderSuccess = false; 
  orderSuccessFoodName = ''; // Store food name separately for success popup
  orderDate = ''; 

  order: Orders = {
    orderStatus: 'Pending',
    totalAmount: 0,
    quantity: 0,
    foodId: 0,
    orderDate: '',
    userId: 0, 
    user: { userId: 0 } as User, 
    food: { foodId: 0 } as Food
  };
  currentPage = 1;
  itemsPerPage = 4; // You can adjust this number
  paginatedFoods: Food[] = [];

  
  search = '';
  noItemFound = false;

  constructor(
    private foodService: FoodService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getAllFoods();
  }

  getAllFoods(): void {
    this.isLoading = true;
    this.foodService.getAllFoods().subscribe({
      next: (data) => {
        this.foods = data;
        this.currentPage = 1;
        this.updatePaginatedFoods();
        this.isLoading = false;
        this.noItemFound = false;
      },
      error: (err) => {
        console.error('Error fetching food items:', err);
        alert('Failed to fetch food items.');
        this.isLoading = false;
      }
    });
  }
  
  searchProduct(): void {
    this.foodService.getAllFoods().subscribe(data => {
      this.foods = data;
      if (this.search !== '') {
        this.foods = this.foods.filter(filt =>
          filt.foodName.toLowerCase().includes(this.search.toLowerCase())
        );
        this.noItemFound = this.foods.length === 0;
      } else {
        this.noItemFound = false;
      }
      this.currentPage = 1;
      this.updatePaginatedFoods();
    });
  }
  
  reset(): void {
    this.search = '';
    this.noItemFound = false;
    this.getAllFoods();
  }


  openOrderPopup(food: Food): void {
    this.selectedFood = { ...food }; 
    this.orderQuantity = 1; 
    this.totalAmount = this.selectedFood.price; 
    this.orderDate = new Date().toLocaleDateString(); 
    this.showOrderPopup = true; 
  }

  closeOrderPopup(): void {
    this.showOrderPopup = false;
    this.selectedFood = null; 
  }

  updateTotalAmount(): void {
    if (this.selectedFood) {
      this.totalAmount = this.selectedFood.price * this.orderQuantity;
    }
  }

  confirmOrder(): void {
    if (this.selectedFood) {
      const userId = Number(localStorage.getItem('userId')); 

      this.orderSuccessFoodName = this.selectedFood.foodName; // Store food name before closing popup
      this.order.foodId = this.selectedFood.foodId;
      this.order.quantity = this.orderQuantity;
      this.order.totalAmount = this.totalAmount;
      this.order.orderDate = new Date().toISOString();
      this.order.userId = userId;
      this.order.user.userId = userId;
      this.order.food.foodId = this.selectedFood.foodId;
  
      this.orderService.placeOrder(this.order).subscribe(
        () => {
          this.orderSuccess = true;
          this.closeOrderPopup(); 
        },
        (error) => {
          console.error('Error placing order:', error);
          alert('Failed to place order.');
        }
      );
    }
  }

  closeOrderSuccessPopup(): void {
    this.getAllFoods();
    this.orderSuccess = false; 
  }
  updatePaginatedFoods(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedFoods = this.foods.slice(startIndex, endIndex);
  }
  
  changePage(page: number): void {
    this.currentPage = page;
    this.updatePaginatedFoods();
  }
  
  get totalPages(): number[] {
    return Array(Math.ceil(this.foods.length / this.itemsPerPage)).fill(0).map((_, i) => i + 1);
  }
  
}