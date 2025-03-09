import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { RouterModule } from '@angular/router';
import { Product } from '../product';
import { ProductService } from '../../../services/ProductService';

@Component({
  selector: 'app-list-product-page',
  imports: [
    CommonModule,
    ButtonComponent,
    RouterModule
  ],
  templateUrl: './list-product-page.component.html',
  styleUrl: './list-product-page.component.css'
})
export class ListProductPageComponent {
  productsService: ProductService = inject(ProductService);
  products: Product[] = [];

  constructor() {
    this.productsService.getAllProducts().then((products) => {
      this.products = products;
    });
  }
}
