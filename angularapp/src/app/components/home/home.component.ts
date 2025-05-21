import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import * as AOS from 'aos';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public authservice:AuthService) { }
    
  isLoading = true;
  email:string ='foodmart@gmail.com';
  ngOnInit(): void {
    setTimeout(() => {
      this.isLoading = false;
      AOS.init(); 
    }, 2500); 
  }
}