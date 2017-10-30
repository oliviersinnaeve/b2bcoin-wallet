import { Component, ViewChild, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';

import * as userModels from '../../../services/com.b2beyond.api.user/model/models';
import { UserApi } from '../../../services/com.b2beyond.api.user/api/UserApi'

import { websiteId } from '../../../environment';
import { UserState } from '../../../user.state';

import 'style-loader!./settings.scss';


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

    public secretKeyUrl: string = "";
    public codeToSubmit: number;

    public submitted: boolean = false;

    constructor (
        private userApi: UserApi,
        private userState: UserState) {

        this.userApi.defaultHeaders = userState.getExtraHeaders();
    }

    public ngOnInit (): void {
        this.user = this.userState.getUser();

        this.is2faEnabled = this.user.enabled2FA;
    }

    public checkbox2faClicked(event) {
        console.log(event);
        if (this.is2faEnabled) {

            this.userApi.requestEnable2FA({websiteId: websiteId}).subscribe(
                result => {
                    console.log("The qr response", result);
                    this.secretKeyUrl = result.qrImageUrl;
                    this.userState.getUser().secretKey = result.secretKey;
                },
                error => {
                    console.log("The qr error", error);
                }
            );

            this.enable2FAModal.show();
        } else {
            this.disable2FAModal.show();
        }
    }

    public enable2FA() {
        this.userApi.enable2FA({code2FA: this.codeToSubmit, websiteId: websiteId, userId: this.userState.getUser().userId, secretKey: this.userState.getUser().secretKey}).subscribe(
                result => {
                    console.log("The qr response", result);

                    this.enable2FAModal.hide();
                    this.twoFASuccessModal.show();
            },
                error => {
                    console.log("The qr error", error);
            }
        );
    }

    public disable2FA() {
        this.userApi.disable2FA({websiteId: websiteId, userId: this.userState.getUser().userId, secretKey: this.userState.getUser().secretKey}).subscribe(
                result => {
                    this.userState.setUser(result);
                    this.disable2FAModal.hide();
                    this.disableTwoFASuccessModal.show();
            },
                error => {
                console.log("The qr error", error);
            }
        );
    }

}
