import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  userForm!: FormGroup

  constructor(private fb: FormBuilder, private userService: UserService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required]
    });

    const userId = Number(this.route.snapshot.paramMap.get('userId'));

    if (userId) {
      this.userService.getUserById(userId).subscribe((user) => {
        this.userForm?.patchValue({
          email: user.email,
          role: user.role
        });
      });
    }
  }
}
