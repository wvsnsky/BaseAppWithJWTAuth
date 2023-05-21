import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import {JwtHelperService, JwtModule} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('accessToken');
    const jwtModule = new JwtHelperService();
    if (token) {
      const decodedToken = jwtModule.decodeToken(token);
      const userRoles = decodedToken.roles;
      if (userRoles.includes('ROLE_ADMIN')) {
        return true;
      } else {
        this.router.navigate(['home']);
        return false;
      }
    }
    this.router.navigate(['login']);
    return false;
  }
}
