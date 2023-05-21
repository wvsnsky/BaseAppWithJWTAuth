import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "../../services/login/login.service";
import {UserAuth} from "../../models/user-auth/user-auth.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  userAuth!: UserAuth | null;
  errorMessage = '';
  constructor(private formBuilder: FormBuilder, private loginService: LoginService, private router: Router) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;
    this.loginService.login({email, password}).subscribe(
      res => {
        this.userAuth = res.body;
        if (this.userAuth) {
          localStorage.setItem('accessToken', this.userAuth.accessToken);
          this.router.navigate(['/home']);
        }
      },
      err => {
        this.errorMessage = 'Bad credentials!';
      }
    )
  }


}
