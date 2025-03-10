import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePromotionFormPageComponent } from './update-promotion-form-page.component';

describe('UpdatePromotionFormPageComponent', () => {
  let component: UpdatePromotionFormPageComponent;
  let fixture: ComponentFixture<UpdatePromotionFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdatePromotionFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatePromotionFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
