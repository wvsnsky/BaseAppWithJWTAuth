import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";
import {Router} from "@angular/router";
import {RoleService} from "../../services/role/role.service";

@Component({
  selector: 'app-bars',
  templateUrl: './bars.component.html',
  styleUrls: ['./bars.component.scss']
})
export class BarsComponent {
  constructor(private router: Router, private roleService: RoleService) {}
  @ViewChild('sidenav') sidenav: MatSidenav | undefined;

  toggleSidenav() {
    this.sidenav?.toggle();
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean {
    return this.roleService.getCurrentUserRoles().includes('ROLE_ADMIN');
  }

}
