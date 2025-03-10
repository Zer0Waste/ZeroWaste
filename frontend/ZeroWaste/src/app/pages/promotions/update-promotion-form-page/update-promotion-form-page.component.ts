import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../../components/form/input/input.component';
import { InputWithSymbolComponent } from '../../../components/form/input-with-symbol/input-with-symbol.component';
import { TextareaComponent } from '../../../components/form/textarea/textarea.component';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { API_URL } from '../../../utils/contants';

@Component({
  selector: 'app-update-promotion-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    InputWithSymbolComponent,
    TextareaComponent,
    ButtonComponent,
  ],
  templateUrl: './update-promotion-form-page.component.html',
  styleUrl: './update-promotion-form-page.component.css'
})
export class UpdatePromotionFormPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

  // Form
  public promotionForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    percentage: ['', [Validators.required, Validators.min(1), Validators.max(100)]],
    startsAt: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
    endsAt: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
  });

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(
      this.promotionForm.get(controlName)!
    );

    return validationErrorMessage;
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    if (this.promotionForm.invalid) {
      return;
    }

    try {
      const response = await this.updatePromotion();

      if (!response.ok) {
        switch (response.status) {
          case 401:
            alert('Você não tem permissão para atualizar produtos');
            break;

          case 404:
            alert('Produto não encontrado');
            break;

          default:
            alert('Erro ao atualizar produto');
            break;
        }

        return;
      }

      alert('Promoção atualizada com sucesso');

      this.router.navigate(['/home']);

    }

    catch (error) {
      console.error('Erro ao atualizar promoção', error);
      alert('Erro ao atualizar promoção');
    }
  }


  public async ngOnInit(): Promise<void> {
    this.promotionForm.disable();



    try {
      const response = await this.getPromotion();

      if (!response.ok) {
        switch (response.status) {
          case 401:
            alert('Você não tem permissão para atualizar promoções');
            break;

          case 404:
            alert('Promoção não encontrada');
            break;

          default:
            alert('Erro ao atualizar promoção');
            break;
        }

        return;
      }

      const { promotion } = await response.json();

      this.promotionForm.setValue({
        name: promotion.name,
        percentage: (promotion.percentage * 100).toString(),
        startsAt: promotion.startsAt,
        endsAt: promotion.endsAt,
      });
    }

    catch (error) {
      console.error('Erro ao buscar promoção', error);
      alert('Erro ao buscar promoção');
    }

    finally {
      this.promotionForm.enable();
    }
  }

  public async getPromotion() {
    const promotionId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/promotions/" + promotionId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });

  }

  public async updatePromotion() {
    const promotionId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/promotions/" + promotionId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(this.promotionForm.value),
    });
  }


}
