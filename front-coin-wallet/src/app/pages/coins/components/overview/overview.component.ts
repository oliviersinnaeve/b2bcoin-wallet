import {Component, ViewChild, Input, OnInit} from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletServiceStore } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletService } from '../../../../services/com.b2beyond.api.b2bcoin';

import 'rxjs/Rx';

@Component({
    selector: 'coins-overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class CoinOverview implements OnInit {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    public creatingWallet = false;

    public selectedCoin = {};

    constructor (private userState: UserState,
                 private WalletService: WalletService,
                 private walletService: WalletServiceStore,
                 private router: Router) {
    }


    ngOnInit(): void {
        this.WalletService.defaultHeaders = this.userState.getExtraHeaders(this.WalletService.defaultHeaders);
    }

    public createAddress (coin) {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.selectedCoin = coin;
            this.WalletService.createAddress(coin.name).subscribe(
                    result => {
                        this.creatingWallet = false;
                        this.walletService.addresses = [];
                        this.walletService.addressBalances = {};
                        this.walletService.getAddresses(false);
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
