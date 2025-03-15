import { CommonModule, formatDate } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { InputComponent } from '../../../components/form/input/input.component';
import { SelectComponent } from '../../../components/form/select/select.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PromotionService } from '../../../services/PromotionService';
import { Promotion } from '../promotion';
import { InputWithSymbolComponent } from '../../../components/form/input-with-symbol/input-with-symbol.component';

@Component({
  selector: 'app-detail-promotion-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    InputComponent,
    RouterModule,
    InputWithSymbolComponent
  ],
  templateUrl: './detail-promotion-page.component.html',
  styleUrl: './detail-promotion-page.component.css'
})
export class DetailPromotionPageComponent {
  fb = inject(FormBuilder);
  promotionService: PromotionService = inject(PromotionService);
  route: ActivatedRoute = inject(ActivatedRoute);
  promotion: Promotion | undefined;

  public products: any[] = [];

  public promotionForm = this.fb.group({
    name: [''],
    percentage: [''],
    startsAt: [''],
    endsAt: [''],
    products: [''],
  });

  public ngOnInit(): void {
    this.promotionForm.disable();

    const promotionId = Number(this.route.snapshot.params['id']);
    this.promotionService.getPromotionById(promotionId).then((promotionResponse => {
      this.promotion = promotionResponse;

      this.promotionForm.setValue({
        name: this.promotion.name,
        percentage: (this.promotion.percentage).toString(),
        startsAt: formatDate(this.promotion.startsAt, 'yyyy-MM-dd', 'en-US'),
        endsAt: formatDate(this.promotion.endsAt, 'yyyy-MM-dd', 'en-US'),
        products: this.promotion.products.map(product => product.name).join(', ')
      });

      this.promotion.products.forEach(product => this.products.push({ name: product.name, id: product.id }));
    }));
  }

}
