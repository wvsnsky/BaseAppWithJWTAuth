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

  constructor(protected userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.findAllUsers().subscribe(res => this.users = res);
  }

}
