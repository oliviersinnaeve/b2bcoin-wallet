import { Component, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';


import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


@Component({
    selector: 'create-payment',
    templateUrl: './createPayment.html',
})

export class CreatePayment {


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.initialize();
    }

    private initialize () {
        //this.componentApi.getFragmentsForSearch({user: this.userState.getUser().userId, websiteIds: [0]}).subscribe(result => {
        //  this.components = result;
        //},
        //(error) => {
        //    if (error.status === 401) {
        //        this.userState.handleError(error, this.initialize, this);
        //    }
        //});
        //
        //this.websiteApi.getWebsitesForSearch({user: this.userState.getUser().userId}).subscribe(result => {
        //  this.websites = result;
        //},
        //(error) => {
        //    if (error.status === 401) {
        //        this.userState.handleError(error, this.initialize, this);
        //    }
        //});
    }

}
