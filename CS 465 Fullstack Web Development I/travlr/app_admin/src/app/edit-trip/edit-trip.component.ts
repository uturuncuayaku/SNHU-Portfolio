import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TripDataService } from '../services/trip-data.service';
import { Trip } from '../models/trip';

@Component({
  selector: 'app-edit-trip',
  templateUrl: './edit-trip.component.html',
  styleUrls: ['./edit-trip.component.css']
})
export class EditTripComponent implements OnInit {
  editForm!: FormGroup;
  submitted = false;
  message: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private tripService: TripDataService
  ) { }

  ngOnInit(): void {
    const tripCode = localStorage.getItem('tripCode');
    if (!tripCode) {
      alert("No trip code found to edit!");
      this.router.navigate(['list-trips']);
      return;
    }

    this.editForm = this.formBuilder.group({
      _id: [],
      code: [tripCode, Validators.required],
      name: ['', Validators.required],
      length: ['', Validators.required],
      start: ['', Validators.required],
      resort: ['', Validators.required],
      perPerson: ['', Validators.required],
      image: ['', Validators.required],
      description: ['', Validators.required]
    });

    this.message = 'Loading trip details...';
    this.tripService.getTrip(tripCode).subscribe({
      next: (data: Trip[]) => {
        if (data && data.length > 0) {
          const trip = data[0];
          // Format date for date input (YYYY-MM-DD)
          let startDateStr = '';
          if (trip.start) {
            startDateStr = new Date(trip.start).toISOString().split('T')[0];
          }
          this.editForm.patchValue({
            _id: trip._id,
            code: trip.code,
            name: trip.name,
            length: trip.length,
            start: startDateStr,
            resort: trip.resort,
            perPerson: trip.perPerson,
            image: trip.image,
            description: trip.description
          });
          this.message = `Editing Trip: ${trip.name} (${trip.code})`;
        } else {
          this.message = 'Trip not found';
        }
      },
      error: (err) => {
        console.error('Error fetching trip:', err);
        this.message = 'Error loading trip';
      }
    });
  }

  public onSubmit(): void {
    this.submitted = true;
    if (this.editForm.valid) {
      this.tripService.updateTrip(this.editForm.value).subscribe({
        next: (data) => {
          console.log('Trip updated successfully:', data);
          this.router.navigate(['list-trips']);
        },
        error: (err) => {
          console.error('Error updating trip:', err);
        }
      });
    }
  }

  get f() { return this.editForm.controls; }
}
