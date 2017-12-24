import { Component, ViewChild, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';
import { TransactionsService } from '../../transactions/transactions.service';


import 'style-loader!./walletInfoFull.scss';

import * as b2bcoinModels from '../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


@Component({
    selector: 'wallet-info-full',
    templateUrl: './walletInfoFull.html'
})
export class WalletInfoFull {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    // Wallet info
    public numberOfAddresses: number;
    public balance: number = 0;
    public lockedBalance: number = 0;
    public creatingWallet = false;

    // Server info
    public difficulty: number = 0;


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private transactionsService: TransactionsService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.walletService.getAddresses();
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.walletApi.createAddress(this.walletService.primaryCoin.name).subscribe(
                    result => {
                        this.walletService.addresses = [];
                        this.walletService.addressBalances = {};
                        this.walletService.getAddresses();
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
