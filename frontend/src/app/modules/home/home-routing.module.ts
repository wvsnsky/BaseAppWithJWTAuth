import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "../../components/home/home.component";
import {UserManagementComponent} from "../../components/user-management/user-management.component";
import {AuthGuard} from "../../guards/auth.guard";
import {AdminGuard} from "../../guards/admin.guard";
import {ContactComponent} from "../../components/contact/contact.component";
import { EditUserComponent } from 'src/app/components/edit-user/edit-user.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'user-management',
        canActivate: [AdminGuard],
        children: [
          { path: '', component: UserManagementComponent },
          { path: 'edit/:userId', component: EditUserComponent }
        ]
      },
      {
        path: 'contact', component: ContactComponent, canActivate: [AuthGuard]
      }
    ],
    canActivate: [AuthGuard],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
