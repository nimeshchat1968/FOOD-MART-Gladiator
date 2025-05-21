import { Component, OnInit } from '@angular/core';
import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food.service';

@Component({
  selector: 'app-adminviewfood',
  templateUrl: './adminviewfood.component.html',
  styleUrls: ['./adminviewfood.component.css']
})
export class AdminviewfoodComponent implements OnInit {
  foods: Food[] = [];
  paginatedFoods: Food[] = [];
  selectedFood: Food | null = null;
  showPopup = false;
  showDialog = false;
  foodToDelete: number | null = null;
  isLoading = false;

  pageSize = 4;
  currentPage = 1;
  totalPages: number[] = [];

  constructor(private readonly foodService: FoodService) {}

  ngOnInit(): void {
    this.getAllFoods();
  }

  getAllFoods(): void {
    this.isLoading = true;
    this.foodService.getAllFoods().subscribe({
      next: (data) => {
        this.foods = data;
        this.calculatePagination();
        this.updatePaginatedFoods();
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error fetching food items:', err);
        alert('Failed to fetch food items.');
      }
    });
  }

  calculatePagination(): void {
    const totalPagesCount = Math.ceil(this.foods.length / this.pageSize);
    this.totalPages = Array.from({ length: totalPagesCount }, (_, i) => i + 1);
  }

  updatePaginatedFoods(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedFoods = this.foods.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.updatePaginatedFoods();
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedFoods();
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages.length) {
      this.currentPage++;
      this.updatePaginatedFoods();
    }
  }

  openEditPopup(food: Food): void {
    this.selectedFood = { ...food };
    this.showPopup = true;
  }

  closePopup(): void {
    this.showPopup = false;
    this.selectedFood = null;
  }

  updateFood(): void {
    if (this.selectedFood) {
      this.foodService.updateFood(this.selectedFood.foodId, this.selectedFood).subscribe({
        next: () => {
          this.closePopup();
          this.getAllFoods();
          this.showSuccessDialog = true; // Show success popup
        },
        error: (err) => {
          console.error('Error updating food:', err);
          alert('Failed to update food.');
        }
      });
    }
  }
  

  confirmDeleteFood(foodId: number): void {
    this.foodToDelete = foodId;
    this.showDialog = true;
  }

  deleteFood(): void {
    if (this.foodToDelete !== null) {
      this.foodService.deleteFood(this.foodToDelete).subscribe({
        next: () => {
          this.foods = this.foods.filter(food => food.foodId !== this.foodToDelete);
          this.closeDialog();
          this.calculatePagination();
          this.updatePaginatedFoods();
        },
        error: (err) => {
          console.error('Error deleting food item:', err);
          alert('Failed to delete food item. Error: ' + err.message);
        }
      });
    }
  }

  closeDialog(): void {
    this.showDialog = false;
    this.foodToDelete = null;
  }

  onDialogConfirm(confirm: boolean): void {
    if (confirm) {
      this.deleteFood();
    } else {
      this.closeDialog();
    }
  }
  showSuccessDialog = false;

closeSuccessDialog(): void {
  this.showSuccessDialog = false;
}

}
