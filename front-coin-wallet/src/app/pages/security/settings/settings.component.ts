import { Component, ViewChild, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';

import * as userModels from '../../../services/com.b2beyond.api.user/model/models';
import { UserService } from '../../../services/com.b2beyond.api.user'

import { websiteId } from '../../../environment-config';
import { UserState } from '../../../user.state';

import 'style-loader!./settings.scss';
import {Enable2FARequest} from "../../../services/com.b2beyond.api.user/model/enable2FARequest";
import {Disable2FARequest} from "../../../services/com.b2beyond.api.user/model/disable2FARequest";


@Component({
    selector: 'settings',
    templateUrl: './settings.html',
})
export class Settings implements OnInit {

    @ViewChild('enable2FAModal') enable2FAModal: ModalDirective;
    @ViewChild('disable2FAModal') disable2FAModal: ModalDirective;
    @ViewChild('twoFASuccessModal') twoFASuccessModal: ModalDirective;
    @ViewChild('disableTwoFASuccessModal') disableTwoFASuccessModal: ModalDirective;



    public messages: Array<string> = [];
    public errors: Array<string> = [];

    public user: userModels.User = {};
    public is2faEnabled: boolean = false;
    public failed: boolean = false;
    public error: string;

    public secretKeyUrl: string = "";
    public secretKey: string = "";
    public codeToSubmit: number;

    public submitted: boolean = false;

    constructor (
        private UserService: UserService,
        private userState: UserState) {
    }

    public ngOnInit (): void {
        this.UserService.defaultHeaders = this.userState.getExtraHeaders(this.UserService.defaultHeaders);

        this.user = this.userState.getUser();

        this.is2faEnabled = this.user.enabled2FA;
    }

    public checkbox2faClicked(event) {
        //console.log(event);
        if (this.is2faEnabled) {

            this.UserService.requestEnable2FA({websiteId: websiteId}).subscribe(
                result => {
                    //console.log("The qr response", result);
                    this.secretKeyUrl = result.qrImageUrl;
                    this.secretKey = result.secretKey;
                    // this.userState.getUser(). = result.secretKey;
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
        let request: Enable2FARequest = {};
        request.user = {code2FA: this.codeToSubmit, websiteId: websiteId, userId: this.userState.getUser().userId};
        request.secretKey = this.secretKey;

        this.UserService.enable2FA(request).subscribe(
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
        let request: Disable2FARequest = {};
        request.user = {websiteId: websiteId, userId: this.userState.getUser().userId};
        request.secretKey = this.secretKey;
        this.UserService.disable2FA(request).subscribe(
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
