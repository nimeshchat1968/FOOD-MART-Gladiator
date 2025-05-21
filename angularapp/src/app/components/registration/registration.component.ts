import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  user: User = {
    email: '',
    password: '',
    confirmPassword: '',
    username: '',
    mobileNumber: '',
    userRole: 'USER'
  };
  showDialog = false;
  passwordMismatch = false;
  mismatchDialog = false;

  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  onSubmit(form: NgForm): void {
    this.passwordMismatch = this.user.password !== this.user.confirmPassword;
    if (this.passwordMismatch) {
      this.mismatchDialog = true;
      return;
    }
    if (form.valid && !this.passwordMismatch) {
      this.authService.register(this.user).subscribe(() => {
        this.showDialog = true;
      }, (error) => {
        alert('Registration failed.');
      });
    }
  }

  onDialogConfirm(): void {
    this.showDialog = false;
    this.router.navigate(['/login']);
  }

  onMismatchDialogConfirm(): void {
    this.mismatchDialog = false; 
  }
}
