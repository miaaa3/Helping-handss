import { Component, Input } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import { VolunteerServiceService } from '../services/volunteerservice';


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

  volunteer: Volunteer = {} as Volunteer; // Initialize an empty Volunteer object

  constructor(private volunteerService: VolunteerServiceService) { }

  onSubmit(): void {
    console.log(this.volunteer)
    this.volunteerService.register(this.volunteer).subscribe(
      response => {
        console.log('Registration successful:', response);
      },
      error => {
        console.error('Registration failed:', error);
      }
    );
  }
}

