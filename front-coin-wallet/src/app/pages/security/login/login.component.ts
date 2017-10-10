declare var FB;

var CryptoJS = require('crypto-js');

import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormGroup, AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { EmailValidator } from '../../../theme/validators';

import { FacebookService, InitParams, LoginResponse } from 'ngx-facebook';

import * as userModels from '../../../services/com.b2beyond.api.user/model/models';
import { UserApi } from '../../../services/com.b2beyond.api.user/api/UserApi'

import { UserState } from '../../../user.state';

import { websiteId } from '../../../environment';

import 'style-loader!./login.scss';

@Component({
    selector: 'login',
    templateUrl: './login.html',
})
export class Login implements OnInit {

    public messages: Array<string> = [];

    public form: FormGroup;
    public userId: AbstractControl;
    public password: AbstractControl;
    public submitted: boolean = false;

    constructor (private fb: FormBuilder,
                 private userState: UserState,
                 private userApi: UserApi,
                 private router: Router,
                 private facebookService: FacebookService) {
        this.form = fb.group({
            'userId': ['', Validators.compose([Validators.required, EmailValidator.validate])],
            'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
        });

        this.userId = this.form.controls['userId'];
        this.password = this.form.controls['password'];

        // FACEBOOK init is done in the index.html !!! Only way it worked ...
    }

    ngOnInit () {

    }

    public loginWithFacebook (): void {
        this.facebookService.login()
            .then((response: LoginResponse) => console.log(response))
            .catch((error: any) => console.error(error));
    }

    public onSubmit (values: any): void {
        this.messages = [];

        values.websiteId = websiteId;
        //values.password = CryptoJS.AES.encrypt(values.password, values.userId).toString();

        this.submitted = true;
        if (this.form.valid) {
            this.userApi.login(values).subscribe(result => {
                    console.log("Logged in redirecting to dashboard");
                    this.userState.setUser(result);
                    this.router.navigateByUrl("dashboard");
                },
                    error => {
                    this.messages.push("Login failed, wrong credentials");
                });
        }
    }


}
