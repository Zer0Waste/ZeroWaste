import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../../components/form/input/input.component';
import { InputWithSymbolComponent } from "../../../components/form/input-with-symbol/input-with-symbol.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { API_URL } from '../../../utils/contants';

@Component({
  selector: 'app-create-promotion-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    InputWithSymbolComponent,
    TextareaComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './create-promotion-form-page.component.html',
  styleUrl: './create-promotion-form-page.component.css'
})
export class CreatePromotionFormPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);

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
      await this.savePromotion(this.promotionForm.value)
      alert('Promoção salva com sucesso');
      this.router.navigate(['/home']);
    } catch (error) {
      console.error('Erro ao salvar promoção', error);
      alert('Erro ao salvar promoção');
    }
  }

  public async savePromotion(data: typeof this.promotionForm.value) {

    data.percentage = (Number(data.percentage) / 100).toString();

    const response = await fetch(API_URL + "/promotions/", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(data),
    });

    return response.json();
  }


}
