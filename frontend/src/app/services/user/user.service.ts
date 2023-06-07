import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {User} from "../../models/user/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private resourceUrl = '/api/users'
  constructor(private http: HttpClient) { }

  getFilteredUsers(filterCriteria: any, pageNumber: number, pageSize: number): Observable<any> {
    let params = new HttpParams()
      .set('filterCriteria', JSON.stringify(filterCriteria))
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get(this.resourceUrl, { params });
  }

}
