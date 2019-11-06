import { Component, ViewChild, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';

import {WalletResourceService} from "../../../services/com.b2beyond.api.webwallet-service-b2bcoin";


@Component({
    selector: 'wallet-info-full',
    templateUrl: './walletInfoFull.html',
    styleUrls:['./walletInfoFull.scss']
})
export class WalletInfoFull {

    @ViewChild('createAddressModal', {static: false}) createAddressModal: ModalDirective;


    public creatingWallet = false;


    constructor (private userState: UserState,
                 private walletApi: WalletResourceService,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.walletApi.createAddressUsingGET(this.walletService.primaryCoin.name).subscribe(
                    result => {
                        this.walletService.addresses = [];
                        this.walletService.addressBalances = {};
                        this.walletService.getAddresses(false);
                        this.creatingWallet = false;
                        this.createAddressModal.show();
                },
                    error => {
                    if (error.status === 401) {
                        this.creatingWallet = false;
                        this.userState.handleError(error, this.createNewAddress, this);
                    }
                }
            );
        }
    }

    public getNumberOfAddresses() {
        let numberOfAddresses = this.walletService.getNumberOfAddresses(this.walletService.primaryCoin);
        return {'value': numberOfAddresses };
    }

    public createNewPayment() {
        this.walletService.selectedCoin = this.walletService.primaryCoin;
        this.router.navigateByUrl("/pages/payments/create");
    }

}
