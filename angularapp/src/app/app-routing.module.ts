import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { AdminaddfoodComponent } from './components/adminaddfood/adminaddfood.component';
import { AdminorderschartComponent } from './components/adminorderschart/adminorderschart.component';
import { AdminviewfeedbackComponent } from './components/adminviewfeedback/adminviewfeedback.component';
import { AdminviewfoodComponent } from './components/adminviewfood/adminviewfood.component';
import { AdminviewordersComponent } from './components/adminvieworders/adminvieworders.component';
import { ErrorComponent } from './components/error/error.component';
import { UseraddfeedbackComponent } from './components/useraddfeedback/useraddfeedback.component';
import { UserviewfeedbackComponent } from './components/userviewfeedback/userviewfeedback.component';
import { UserviewfoodComponent } from './components/userviewfood/userviewfood.component';
import { UserviewordersComponent } from './components/uservieworders/uservieworders.component';
import { AdminGuard } from './admin.guard';
import { UserGuard } from './user.guard';

const routes: Routes = [
  {path:"register",component:RegistrationComponent},
  {path:"login",component:LoginComponent},
  {path:"",component:HomeComponent},
  {path:"adminAddFood",component:AdminaddfoodComponent,canActivate:[AdminGuard]},
  {path:"adminViewFood",component:AdminviewfoodComponent,canActivate:[AdminGuard]},
  {path:"userViewFood",component:UserviewfoodComponent,canActivate:[UserGuard]},
  {path:"userViewOrders",component:UserviewordersComponent,canActivate:[UserGuard]},
  {path:"adminOrdersChart",component:AdminorderschartComponent,canActivate:[AdminGuard]},
  {path:"adminViewOrders",component:AdminviewordersComponent,canActivate:[AdminGuard]},
  {path:"userAddFeedback",component:UseraddfeedbackComponent,canActivate:[UserGuard]},
  {path:"userViewFeedback",component:UserviewfeedbackComponent,canActivate:[UserGuard]},
  {path:"adminViewFeedback",component:AdminviewfeedbackComponent,canActivate:[AdminGuard]},
 
  {path:'error',component:ErrorComponent},
  {path:"**",redirectTo:"error",pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
