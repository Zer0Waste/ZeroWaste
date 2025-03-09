import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ProductService } from '../../../services/ProductService';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Product } from '../product';
import { ButtonComponent } from '../../../components/form/button/button.component';

@Component({
  selector: 'app-detail-product-page',
  imports: [
    CommonModule,
    ButtonComponent,
    RouterModule
  ],
  templateUrl: './detail-product-page.component.html',
  styleUrl: './detail-product-page.component.css'
})
export class DetailProductPageComponent {
  productService: ProductService = inject(ProductService);
  route: ActivatedRoute = inject(ActivatedRoute);
  product: Product | undefined;

  constructor() {
    const productId = Number(this.route.snapshot.params['id'])
    this.productService.getProductById(productId).then((product => {
      this.product = product;
    }))
  }
}
