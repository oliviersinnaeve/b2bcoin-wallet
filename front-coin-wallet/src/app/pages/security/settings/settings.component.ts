import {Component} from '@angular/core';
import {FormGroup, AbstractControl, FormBuilder, Validators} from '@angular/forms';
import {EmailValidator, EqualPasswordsValidator} from '../../../theme/validators';

import * as userModels from '../../../services/com.b2beyond.api.user/model/models';
import { UserApi } from '../../../services/com.b2beyond.api.user/api/UserApi'

import { websiteId } from '../../../environment';

import 'style-loader!./settings.scss';


@Component({
    selector: 'settings',
    templateUrl: './settings.html',
})
export class Settings {

    public messages: Array<string> = [];
    public errors: Array<string> = [];

    public form: FormGroup;
    public name: AbstractControl;
    public email: AbstractControl;
    public password: AbstractControl;
    public repeatPassword: AbstractControl;
    public passwords: FormGroup;

    public submitted: boolean = false;

    constructor (private fb: FormBuilder,
                 private userApi: UserApi) {

        this.form = fb.group({
            'name': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
            'email': ['', Validators.compose([Validators.required, EmailValidator.validate])],
            'passwords': fb.group({
                'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
                'repeatPassword': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
            }, {validator: EqualPasswordsValidator.validate('password', 'repeatPassword')})
        });

        this.name = this.form.controls['name'];
        this.email = this.form.controls['email'];
        this.passwords = <FormGroup> this.form.controls['passwords'];
        this.password = this.passwords.controls['password'];
        this.repeatPassword = this.passwords.controls['repeatPassword'];
    }

    public onSubmit (values: {name: "", email: "", passwords: {password}}): void {
        this.messages = [];
        this.errors = [];
        let user: userModels.User = {};
        user.websiteId = websiteId;
        user.userDetails = {websiteId: websiteId, userId: values.email, name: values.name, email: values.email};
        user.userId = values.email;
        user.password = values.passwords.password;

        console.log(user);
        this.submitted = true;
        if (this.form.valid) {
            this.userApi.register(user).subscribe(result => {
                    console.log(result);
                    this.messages.push("You have successfully registered, please check your mail and click the activation link");
                },
                    error => {
                    console.log(error);
                    this.errors.push("Registration failed, try again later");
                });
        }
    }
}
