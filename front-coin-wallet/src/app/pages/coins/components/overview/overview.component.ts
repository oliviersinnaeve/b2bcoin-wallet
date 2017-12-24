import { Component, ViewChild, Input } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletService } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import 'rxjs/Rx';

@Component({
    selector: 'coins-overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class CoinOverview {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    public creatingWallet = false;

    public selectedCoin = {};

    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.walletService.getAddresses();
    }

    public createAddress (coin) {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.selectedCoin = coin;
            this.walletApi.createAddress(coin.name).subscribe(
                    result => {
                        this.creatingWallet = false;
                        this.walletService.addresses = [];
                        this.walletService.addressBalances = {};
                        this.walletService.getAddresses();
                        this.createAddressModal.show();
                },
                    error => {
                    if (error.status === 401) {
                        this.creatingWallet = false;
                        this.userState.handleError(error, this.createAddress, this);
                    }
                }
            );
        }
    }



}
