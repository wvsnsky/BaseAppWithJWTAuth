import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {User} from "../../models/user/user.model";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  users: User[] | undefined;
  filterCriteria: any = {
    "email": "2"
  };
  pageNumber: number = 0;
  pageSize: number = 10;

  constructor(protected userService: UserService) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getFilteredUsers(this.filterCriteria, this.pageNumber, this.pageSize)
      .subscribe((response: any) => {
        this.users = response;
      });
  }

}
