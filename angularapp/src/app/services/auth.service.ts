import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';
import { APIURL } from '../constant/api_url';
import { Login } from '../models/login.model';
 
@Injectable({
  providedIn: 'root'
})
export class AuthService {
 
  constructor(private readonly http: HttpClient) { }
 
  register(user: User): Observable<any>{
    return this.http.post<any>(`${APIURL.APIurl}/register`, user);
  }
 
  login(login: Login): Observable<any>{
    return this.http.post<any>(`${APIURL.APIurl}/login`, login);
  }
  isLoggedIn(): boolean {
    return !!localStorage.getItem('userId');
  }
 
  getRole(): string | null {
    return localStorage.getItem('userRole');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }
 
  getUserId(): string | null {
    return localStorage.getItem('userId');
  }
 
  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }
 
  logout(): void {
    localStorage.removeItem('userId');
    localStorage.removeItem('userRole');
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  }
 
  isUser(): boolean {
    let role = localStorage.getItem('userRole');
    return role === 'USER';
  }
 
  isLoggedUser(): boolean {
    let role = localStorage.getItem('userRole');
    return role !== null;
  }
}