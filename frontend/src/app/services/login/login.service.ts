import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserAuth} from "../../models/user-auth/user-auth.model";

export type EntityResponseType = HttpResponse<UserAuth>

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  resourceUrl = '/api/auth/login'

  constructor(private httpClient: HttpClient) { }

  login(payload: any): Observable<EntityResponseType> {
    return this.httpClient.post<UserAuth>(this.resourceUrl, payload, {observe: 'response'})
  }
}
