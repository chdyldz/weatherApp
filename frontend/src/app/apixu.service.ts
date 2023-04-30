import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { ResponseEntity } from './model/ResponseEntity';
import {apiUrl} from './app.component';


@Injectable({
  providedIn: 'root'
})
export class ApixuService {
  myApiKey: string = '814b1cc6f53ac582f374a2e1e342c786';

  constructor(private http: HttpClient) { }

  getWeather(location) {
    return this.http.get(
      'http://localhost:8080/camel/api/weather?&city=' + location
    );
  }

  getConnections() {
    return this.http.get<ResponseEntity>(apiUrl + '?&city=');
  }

  getWhetherByCityAndCountry(city,country) {
    return this.http.get(apiUrl , {
      headers: new HttpHeaders().set('Content-Type', 'application/json').append('city', city).append('country', country)
    });
  }
}
