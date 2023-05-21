import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "../../components/home/home.component";
import {UserManagementComponent} from "../../components/user-management/user-management.component";
import {AuthGuard} from "../../guards/auth.guard";
import {AdminGuard} from "../../guards/admin.guard";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {path: 'user-management', component: UserManagementComponent, canActivate: [AdminGuard]}
    ],
    canActivate: [AuthGuard],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
