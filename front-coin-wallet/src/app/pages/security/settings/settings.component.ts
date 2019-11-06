import { Component, ViewChild, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';

import {User, UserResourceService} from "../../../services/com.b2beyond.api.webwallet-service-user";
import {environment} from "../../../../environments/environment";


@Component({
    selector: 'settings',
    templateUrl: './settings.html',
    styleUrls:['./settings.scss']
})
export class Settings implements OnInit {

    @ViewChild('enable2FAModal', {static: false}) enable2FAModal: ModalDirective;
    @ViewChild('disable2FAModal', {static: false}) disable2FAModal: ModalDirective;
    @ViewChild('twoFASuccessModal', {static: false}) twoFASuccessModal: ModalDirective;
    @ViewChild('disableTwoFASuccessModal', {static: false}) disableTwoFASuccessModal: ModalDirective;



    public messages: Array<string> = [];
    public errors: Array<string> = [];

    public user: User = {};
    public is2faEnabled: boolean = false;
    public failed: boolean = false;
    public error: string;

    public secretKeyUrl: string = "";
    public secretKey: string = "";
    public codeToSubmit: number;

    public submitted: boolean = false;

    constructor (
        private userApi: UserResourceService,
        private userState: UserState) {

        this.userApi.defaultHeaders = userState.getExtraHeaders();
    }

    public ngOnInit (): void {
        this.user = this.userState.getUser();

        this.is2faEnabled = this.user.enabled2FA;
    }

    public checkbox2faClicked(event) {
        //console.log(event);
        if (this.is2faEnabled) {

            this.userApi.requestEnable2FAUsingPOST({websiteId: environment.websiteId}).subscribe(
                result => {
                    //console.log("The qr response", result);
                    this.secretKeyUrl = result.qrImageUrl;
                    this.secretKey = result.secretKey;
                    this.userState.getUser().secretKey = result.secretKey;
                },
                error => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.checkbox2faClicked, this);
                    }
                }
            );

            this.enable2FAModal.show();
        } else {
            this.disable2FAModal.show();
        }
    }

    public enable2FA() {
        this.userApi.enable2FAUsingPOST({user: this.userState.getUser(), secretKey: this.userState.getUser().secretKey}).subscribe(
                result => {
                    this.failed = false;
                    this.error = undefined;
                    this.enable2FAModal.hide();
                    this.twoFASuccessModal.show();
            },
                error => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.enable2FA, this);
                    }
                    if (error.status == 412) {
                        this.failed = true;
                        this.error = JSON.parse(error._body).error;
                    }
                    console.log("The qr error", error);
            }
        );
    }

    public disable2FA() {
        this.userApi.disable2FAUsingPOST({user: this.userState.getUser(), secretKey: this.userState.getUser().secretKey}).subscribe(
                result => {
                    this.userState.setUser(result);
                    this.disable2FAModal.hide();
                    this.disableTwoFASuccessModal.show();
            },
                error => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.disable2FA, this);
                    }
                    console.log("The qr error", error);
                }
        );
    }

}
