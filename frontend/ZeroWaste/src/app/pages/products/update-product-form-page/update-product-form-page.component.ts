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
  standalone: true, 
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
    promotions: [[]], 
  });

  public promotionsList: { id: number; name: string }[] = [];
  public isLoadingPromotions: boolean = true;
  public hasErrorLoadingPromotions: boolean = false;

  public getErrorMessage(controlName: string): string | null {
    return this.validationErrorMessage.getValidationErrorMessage(this.productForm.get(controlName)!);
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
      await this.getPromotions();
      await this.loadProduct();
    } catch (error) {
      console.error('Erro ao inicializar o formulário', error);
    } finally {
      this.productForm.enable();
    }
  }

  private async getPromotions() {
    try {
      const response = await fetch(API_URL + "/promotions", {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
      });

      if (!response.ok) throw new Error(`Erro HTTP: ${response.status}`);

      const { promotions } = await response.json();
      if (Array.isArray(promotions)) {
        this.promotionsList = [...promotions];
      } else {
        throw new Error("Resposta inválida da API");
      }
    } catch (error) {
      console.error('Erro ao buscar promoções:', error);
      this.hasErrorLoadingPromotions = true;
      alert('Erro ao buscar promoções');
    } finally {
      this.isLoadingPromotions = false;
    }
  }

  private async loadProduct() {
    const productId = this.route.snapshot.paramMap.get('id')!;

    try {
      const response = await fetch(`${API_URL}/products/${productId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
      });

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
        promotions: product.promotions.map((p: any) => p.id),
      });
    } catch (error) {
      console.error('Erro ao buscar produto:', error);
      alert('Erro ao buscar produto');
    }
  }

  private async updateProduct() {
    const productId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(`${API_URL}/products/${productId}`, {
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

