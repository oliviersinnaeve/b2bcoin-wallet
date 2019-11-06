import {Component} from '@angular/core';
import {FormGroup, AbstractControl, FormBuilder, Validators} from '@angular/forms';
import {EmailValidator, EqualPasswordsValidator} from '../../../theme/validators';

import { TranslateService } from '@ngx-translate/core';

import {User, UserResourceService} from "../../../services/com.b2beyond.api.webwallet-service-user";
import {environment} from "../../../../environments/environment";


@Component({
    selector: 'register',
    templateUrl: './register.html',
    styleUrls:['./register.scss']
})
export class Register {

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
                 private userApi: UserResourceService,
                 private translate: TranslateService) {

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

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        let language = navigator.languages && navigator.languages[0].split("-")[0];
        console.log("Using language", language);
        translate.use(language);
    }

    public onSubmit (values: {name: "", email: "", passwords: {password}}): void {
        this.messages = [];
        this.errors = [];
        let user: User = {};
        user.websiteId = environment.websiteId;
        user.userDetails = {websiteId: user.websiteId, userId: values.email, name: values.name, email: values.email};
        user.userId = values.email;
        user.password = values.passwords.password;


        this.submitted = true;
        if (this.form.valid) {
            this.userApi.registerUsingPOST(user).subscribe(result => {
                    //console.log(result);
                    this.messages.push("You have successfully registered, please check your mail and click the activation link");
                },
                    error => {
                    //console.log(error);
                    this.errors.push("Registration failed, try again later");
                });
        }
    }
}
