import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { InputComponent } from "../../../components/form/input/input.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { API_URL } from '../../../utils/contants';

@Component({
  selector: 'app-update-product-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    TextareaComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './update-product-form-page.component.html',
  styleUrl: './update-product-form-page.component.css'
})
export class UpdateProductFormPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

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
      const response = await this.updateProduct();

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

      alert('Produto atualizado com sucesso');

      this.router.navigate(['/products']);
    } catch (error) {
      console.error('Erro ao atualizar produto', error);
      alert('Erro ao atualizar produto');
    }
  }

  public async ngOnInit(): Promise<void> {
    this.productForm.disable();

    try {
      const response = await this.getProduct();

      if (!response.ok) {
        switch (response.status) {
          case 401:
            alert('Você não tem permissão para atualizar produtos');
            break;
          case 404:
            alert('Produto não encontrado');
            break;
          default:
            alert('Erro ao buscar produto');
            break;
        }

        return;
      }

      const { product } = await response.json();

      this.productForm.setValue({
        name: product.name,
        description: product.description,
        brand: product.brand,
        category: product.category,
        unitPrice: product.unitPrice,
        stock: product.stock,
        expiresAt: product.expiresAt,
      });
    } catch (error) {
      console.error('Erro ao buscar produto', error);
      alert('Erro ao buscar produto');
    } finally {
      this.productForm.enable();
    }
  }

  public async getProduct() {
    const productId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/products/" + productId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
  }

  public async updateProduct() {
    const productId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/products/" + productId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(this.productForm.value),
    });
  }
}
