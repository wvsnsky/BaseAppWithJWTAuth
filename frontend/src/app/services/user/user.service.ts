import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {User} from "../../models/user/user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private resourceUrl = '/api/users'
  constructor(private http: HttpClient) { }

  findAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.resourceUrl);
  }

}
