import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
 
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent{
  
  loginData={
    email:'',
     password:''
  }
  errorMessage=''
  
  constructor(private readonly authService:AuthService,private readonly router:Router) { }
  login(){
    this.authService.login(this.loginData).subscribe(
      (response) =>{
        localStorage.setItem('userId', response.userId);
        localStorage.setItem('userRole',response.userRole);
        localStorage.setItem('token',response.token);
        localStorage.setItem('username',response.username);
        this.router.navigate(['/'])
      },
      (error) =>{
        this.errorMessage = 'Invalid username or password';
      }
    )
  }

}

