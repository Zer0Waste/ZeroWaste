import { Component } from '@angular/core';
import { LogoutComponent } from '../auth/logout/logout.component';
import { ModalComponent } from "../components/modal/modal.component";
import { ButtonComponent } from "../components/form/button/button.component";

@Component({
  selector: 'app-home',
  imports: [LogoutComponent, ModalComponent, ButtonComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
