import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserServiceService {
  url = 'http://localhost:8080/user';

  constructor(private _http: HttpClient) {}

  getAll() {
    const url = this.url;
    return this._http.get<any>(url);
  }

  getById(id: String) {
    const url = this.url + '/' + id;
    return this._http.get<any>(url);
  }
}
