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

  getFilteredUsers(filterCriteria: any, pageNumber: number, pageSize: number): Observable<User[]> {
    let params = new HttpParams()
      .set('filterCriteria', JSON.stringify(filterCriteria))
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<User[]>(this.resourceUrl, { params });
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.resourceUrl}/${userId}`);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(this.resourceUrl, user);
  }

}
