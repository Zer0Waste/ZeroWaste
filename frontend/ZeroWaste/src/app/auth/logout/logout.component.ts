import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonComponent } from '../../components/form/button/button.component';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  template: `

  <app-button [type]="'button'" (click)="logout()" variant="destructive" *ngIf="isAuthenticated()">
  Logout
  </app-button>
  `,
  styleUrl: './logout.component.css'
})

export class LogoutComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }
}
