import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApixuService } from '../apixu.service';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent implements OnInit {
  public weatherSearchForm: FormGroup;
  public weatherData: any;

  constructor(
    private formBuilder: FormBuilder,
    private apixuService: ApixuService) { }

  ngOnInit(): void {
    this.weatherSearchForm = this.formBuilder.group({
      city: [''],
      country: ['']
    })
  }

  sendToAPIXU(formValues) {
    this.apixuService.getWhetherByCityAndCountry(formValues.city,formValues.country).subscribe(data => {
      this.weatherData = data;
    });
  }

}
