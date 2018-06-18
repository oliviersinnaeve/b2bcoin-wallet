import {WalletServiceStore} from "../../walletService.service";
import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EmailValidator} from '../../../theme/validators';

import {TranslateService} from 'ng2-translate';

import {FacebookService, LoginResponse} from 'ngx-facebook';

import {User} from "../../../services/com.b2beyond.api.user/model/user";
import {UserService} from '../../../services/com.b2beyond.api.user'
import {UserState} from '../../../user.state';

import {websiteId} from '../../../environment-config';

import 'style-loader!./login.scss';

declare var FB;

let CryptoJS = require('crypto-js');

@Component({
    selector: 'login',
    templateUrl: './login.html',
    styleUrls: ['./login.scss']
})
export class Login implements OnInit {

    public messages: Array<string> = [];

    public form: FormGroup;
    public userId: AbstractControl;
    public password: AbstractControl;
    public code2FA: AbstractControl;
    public submitted: boolean = false;
    public show2FA: boolean = false;


    constructor (private fb: FormBuilder,
                 private walletService: WalletServiceStore,
                 private userState: UserState,
                 private UserService: UserService,
                 private router: Router,
                 private facebookService: FacebookService,
                 private translate: TranslateService) {
        this.form = fb.group({
            'userId': ['', Validators.compose([Validators.required, EmailValidator.validate])],
            'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])],
            'code2FA': ['', Validators.compose([Validators.minLength(6), Validators.maxLength(6)])]
        });

        this.userId = this.form.controls['userId'];
        this.password = this.form.controls['password'];
        this.code2FA = this.form.controls['code2FA'];

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        console.log("Using language", language);
        translate.use(language);
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

        // values.websiteId = websiteId;
        // values.password = CryptoJS.AES.encrypt(values.password, values.userId).toString();

        let user: User = {};
        user.websiteId = websiteId;
        user.userId = values.userId;
        user.password = values.password;
            // CryptoJS.AES.encrypt(values.password, values.userId).toString();


        this.submitted = true;
        if (this.form.valid) {
            this.UserService.login(user).subscribe(result => {
                    //console.log("Logged in redirecting to dashboarèd");
                    this.userState.setUser(result);
                    if (this.userState.getUser().enabled2FA && !this.userState.getUser().valid) {
                        this.show2FA = true;
                        this.submitted = false;
                    } else {
                        this.walletService.initializeWallet(this.userState);
                        this.router.navigateByUrl("dashboard");
                    }
                },
                    error => {
                        this.messages.push("Login failed, wrong credentials");
                        this.submitted = false;
                });
        }
    }


}
