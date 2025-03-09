import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListProductPageComponent } from './list-product-page.component';

describe('ListProductPageComponent', () => {
  let component: ListProductPageComponent;
  let fixture: ComponentFixture<ListProductPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListProductPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListProductPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
