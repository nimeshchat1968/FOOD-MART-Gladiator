import { Food } from "./food.model";
import { User } from "./user.model";

export interface Orders {
    orderId?: number; 
    orderStatus: string;
    totalAmount: number;
    quantity:number;
    userId: number;
    user?:User;
    food?:Food;
    foodId:number;
    orderDate:string;
    }