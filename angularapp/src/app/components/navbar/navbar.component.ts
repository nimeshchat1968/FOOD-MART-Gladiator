import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(public readonly authService:AuthService,private readonly router:Router) { }
  showDialog = false;
  
  logout(): void {
    this.showDialog = true;
  }
    
  onDialogConfirm(result: boolean): void {
    this.showDialog = false;
    if (result) {
    this.authService.logout();
    this.router.navigate(['/login']);
    }
  }
}
