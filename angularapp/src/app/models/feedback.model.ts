import { Food } from "./food.model";
import { User } from "./user.model";

export class Feedback {
    feedbackId?: number;
    feedbackText: string;
    date?: Date;
    rating:number
    userId?:number;
    user?:User
    foodId?:number;
    food?:Food
   }