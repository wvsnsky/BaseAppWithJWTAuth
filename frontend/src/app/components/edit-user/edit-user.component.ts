import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user/user.model';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  userForm!: FormGroup
  userId?: number

  constructor(private fb: FormBuilder, private userService: UserService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.createUserForm();
    this.fillFormWithUserData();
  }

  private createUserForm() {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required]
    });
  }

  private fillFormWithUserData() {
    if (this.userId) {
      this.userService.getUserById(this.userId).subscribe((user) => {
        this.userForm?.patchValue({
          email: user.email,
          role: user.role
        });
      });
    }
  }

  private createUserFromForm(): User {
    return {
      id: this.userId!,
      email: this.userForm.get('email')?.value,
      role: this.userForm.get('role')?.value
    }
  }

  redirectToUserManagement() {
    this.router.navigate(['/user-management'])
  }

  onSubmit() {
    // if creating new user will be added, to do updating this for post method createUser also

    if (this.userForm.invalid) {
      return;
    }

    const user = this.createUserFromForm();
    this.userService.updateUser(user).subscribe(() => {this.redirectToUserManagement()});
  }

}
