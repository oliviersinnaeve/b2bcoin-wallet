import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';

import * as Chart from 'chart.js';

import 'style-loader!./walletInfo.scss';

import * as b2bcoinModels from '../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


@Component({
    selector: 'wallet-info',
    templateUrl: './walletInfo.html'
})
export class WalletInfo {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    public numberOfAddresses: number;
    public balance: number = 0;
    public lockedBalance: number = 0;
    public creatingWallet = false;

    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.initialize();
    }

    public getBalance (): string {
        return (this.balance / 1000000000000).toFixed(12) + " B2B";
    }

    public getLockedBalance (): string {
        return (this.lockedBalance / 1000000000000).toFixed(12) + " B2B";
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.walletApi.createAddress().subscribe(
                    result => {
                        this.initialize();
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

    private initialize () {
        this.walletService.getAddressesObservable().subscribe(result => {
            this.numberOfAddresses = result.length;
            for (var i = 0; i < result.length; i++) {
                this.walletService.getBalanceObservable(result[i].address).subscribe(result => {
                        this.balance += result.availableBalance;
                        this.lockedBalance += result.lockedAmount;
                    },
                    (error) => {
                        if (error.status === 401) {
                            this.userState.handleError(error, this.initialize, this);
                        }
                    });
            }
        },
        (error) => {
            if (error.status === 401) {
                this.userState.handleError(error, this.initialize, this);
            }
        });
    }

}
