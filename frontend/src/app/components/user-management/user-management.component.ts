import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {User} from "../../models/user/user.model";
import {take} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  users: User[] | undefined;
  filterCriteria: any = {};
  pageNumber: number = 0;
  pageSize: number = 10;

  constructor(protected userService: UserService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getFilteredUsers(this.filterCriteria, this.pageNumber, this.pageSize)
      .pipe(take(1))
      .subscribe((response: User[]) => {
        this.users = response;
      });
  }

}
