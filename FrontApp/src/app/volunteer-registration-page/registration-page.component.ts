import { Component, Input } from '@angular/core';


@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent {
  @Input() interests: string[] = [
    'Health and Medical Services', 'Education and Learning',
    'Animal Welfare','Environmental Conservation',  
    'Community Development','Disability Services',
    'Youth and Child Services',
    'Human Rights and Advocacy',
    'Elderly Care and Support',
  ];
  selectedInterests: Set<string> = new Set<string>();

  isSelected(interest: string): boolean {
    return this.selectedInterests.has(interest);
  }

  toggleSelection(interest: string): void {
    if (this.isSelected(interest)) {
      this.selectedInterests.delete(interest);
    } else {
      this.selectedInterests.add(interest);
    }
  }

}
