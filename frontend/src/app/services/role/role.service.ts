import { Injectable } from '@angular/core';
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  userRoles: string[] = [];
  constructor() { }

  getCurrentUserRoles(): string[] {
    const token = localStorage.getItem('accessToken');
    const jwtModule = new JwtHelperService();
    if (token) {
      const decodedToken = jwtModule.decodeToken(token);
      this.userRoles = decodedToken.roles;
    }
    return this.userRoles;
  }
}
