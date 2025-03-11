import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-select',
  imports: [],
  template: `
    <select [name]="name" [id]="id" [value]="value" [disabled]="disabled" [multiple]= "multiple" >
      @if (placeholder) {
        <option disabled selected>{{ placeholder }}</option>
      }
      <ng-content />
    </select>
  `,
  styleUrl: './select.component.css',
  standalone: true,
  host: {
    '[value]': 'value',
    '(change)': 'onSelectChange($event)',
    '(blur)': 'onTouch($event)'
  },
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ]
})
export class SelectComponent implements ControlValueAccessor {
  @Input() id: HTMLSelectElement['id'] = '';
  @Input() name: HTMLSelectElement['name'] = '';
  @Input() value: HTMLSelectElement['value'] = '';
  @Input() placeholder: string = '';
  @Input() disabled: HTMLSelectElement['disabled'] = false;
  @Input() multiple: boolean = false;

  onChange: any = () => {};
  onTouch: any = () => {};

  constructor() {}

  writeValue(value: any): void {
    this.value = value;
    this.onChange(value);
    this.onTouch(value);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onSelectChange(event: Event): void {
    this.writeValue((event.target as HTMLSelectElement).value);
    this.onChange((event.target as HTMLSelectElement).value);
  }
}
