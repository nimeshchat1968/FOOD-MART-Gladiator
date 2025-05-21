import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Orders } from '../models/orders.model';
import { Observable } from 'rxjs';
import { APIURL } from '../constant/api_url';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private readonly http: HttpClient) { }


  placeOrder(order: Orders): Observable<Orders> {
    return this.http.post<Orders>(`${APIURL.APIurl}/orders`, order);
  }

  getAllOrders(): Observable<Orders[]> {
    return this.http.get<Orders[]>(`${APIURL.APIurl}/orders`);
  }

  getOrderById(orderId: number): Observable<Orders> {
    return this.http.get<Orders>(`${APIURL.APIurl}/orders/${orderId}`);
  }

  getAllOrdersByUserId(userId: number): Observable<Orders[]> {
    return this.http.get<Orders[]>(`${APIURL.APIurl}/orders/user/${userId}`);
  }

  updateOrder(orderId: number, orders: Orders): Observable<Orders> {
    return this.http.put<Orders>(`${APIURL.APIurl}/orders/${orderId}`, orders);
  }

  deleteOrder(orderId: number): Observable<void> {
    return this.http.delete<void>(`${APIURL.APIurl}/orders/${orderId}`);
  }
}
