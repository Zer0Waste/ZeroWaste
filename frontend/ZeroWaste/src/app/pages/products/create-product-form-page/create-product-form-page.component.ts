import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { InputComponent } from "../../../components/form/input/input.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { API_URL } from '../../../utils/contants';

@Component({
  selector: 'app-create-product-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    TextareaComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './create-product-form-page.component.html',
  styleUrl: './create-product-form-page.component.css'
})
export class CreateProductFormPageComponent {
  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);

  public productForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]],
    brand: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    category: ['', [Validators.required]],
    unitPrice: ['', [Validators.required, Validators.min(0.00)]],
    stock: ['', [Validators.required, Validators.min(0)]],
    expiresAt: ['', [Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
  });

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.productForm.get(controlName)!);

    return validationErrorMessage;
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    if (this.productForm.invalid) {
      return;
    }

    try {
      await this.saveProduct(this.productForm.value)
      alert('Produto salvo com sucesso');
      this.router.navigate(['/products']);
    } catch (error) {
      console.error('Erro ao salvar produto', error);
      alert('Erro ao salvar produto');
    }
  }

  public async saveProduct(data: typeof this.productForm.value) {
    const response = await fetch(API_URL + "/products", {
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
