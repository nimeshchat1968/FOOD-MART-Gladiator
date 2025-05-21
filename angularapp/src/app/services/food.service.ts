import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Food } from '../models/food.model';
import { APIURL } from '../constant/api_url';

@Injectable({
  providedIn: 'root'
})
export class FoodService {

  constructor(private readonly http: HttpClient) {}

  getAllFoods(): Observable<Food[]> {
    return this.http.get<Food[]>(`${APIURL.APIurl}/food`);
  }

  getFoodById(foodId: number): Observable<Food> {
    return this.http.get<Food>(`${APIURL.APIurl}/food/${foodId}`);
  }

  addFood(food: Food): Observable<Food> {
    return this.http.post<Food>(`${APIURL.APIurl}/food`, food);
  }

  updateFood(foodId: number, food: Food): Observable<Food> {
    return this.http.put<Food>(`${APIURL.APIurl}/food/${foodId}`, food);
  }

  deleteFood(foodId: number): Observable<void> {
    return this.http.delete<void>(`${APIURL.APIurl}/food/${foodId}`);
  }


}