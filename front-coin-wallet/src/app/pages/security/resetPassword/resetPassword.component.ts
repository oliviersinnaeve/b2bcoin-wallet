import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EqualPasswordsValidator} from '../../../theme/validators';

import {TranslateService} from '@ngx-translate/core';

import {UserState} from '../../../user.state';

import {User, UserResourceService} from "../../../services/com.b2beyond.api.webwallet-service-user";

@Component({
    selector: 'resetPassword',
    templateUrl: './resetPassword.html',
    styleUrls:['./resetPassword.scss']
})
export class ResetPassword {

    private websiteId: number;
    private resetToken: string;

    public success: boolean = false;
    public messages: Array<string> = [];

    public form: FormGroup;
    public password: AbstractControl;
    public repeatPassword: AbstractControl;
    public passwords: FormGroup;
    public submitted: boolean = false;

    constructor (private fb: FormBuilder,
                 private userState: UserState,
                 private userApi: UserResourceService,
                 private router: Router,
                 private route: ActivatedRoute,
                 private translate: TranslateService) {

        this.form = fb.group({
            'passwords': fb.group({
                'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
                'repeatPassword': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
            }, {validator: EqualPasswordsValidator.validate('password', 'repeatPassword')})
        });

        this.passwords = <FormGroup> this.form.controls['passwords'];
        this.password = this.passwords.controls['password'];
        this.repeatPassword = this.passwords.controls['repeatPassword'];

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        console.log("Using language", language);
        translate.use(language);
    }

    ngOnInit () {
        this.route.params.subscribe(params => {
            this.resetToken = params['resetToken'];
            this.websiteId = params['websiteId'];
        });
    }

    public onSubmit (values: {passwords: { password}}): void {
        //console.log("Submitting password forgot email", values);
        // this.messages = [];
        let user: User = {};
        user.password = values.passwords.password;

        this.submitted = true;
        if (this.form.valid) {
            this.userApi.resetPasswordUsingPOST(this.websiteId, this.resetToken, user).subscribe(result => {
                    this.success = true;
                },
                    error => {
                    this.messages.push("Something went wrong, try again later");
                });
        }
    }
}
