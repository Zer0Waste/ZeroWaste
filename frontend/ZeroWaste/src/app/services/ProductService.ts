import { Injectable } from "@angular/core";
import { Product } from "../pages/products/product";
import { API_URL } from "../utils/contants";

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    constructor () {}

    async getAllProducts(): Promise<Product[]> {
        const response = await fetch(API_URL + '/products/', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        const { products } =  await response.json();
        return products ?? [];
    }

    async getProductById(id: number): Promise<Product> {
        const response = await fetch(API_URL + '/products/' + id, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        const { product } =  await response.json();
        return product ?? {};
    }
}