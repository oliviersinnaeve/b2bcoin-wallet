import { Component, ViewChild, Input } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletService } from '../../../walletService.service';
import { WalletResourceService } from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin';

import 'rxjs/Rx';

@Component({
    selector: 'coins-overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class CoinOverview {

    @ViewChild('createAddressModal', {static: false}) createAddressModal: ModalDirective;

    public creatingWallet = false;

    public selectedCoin = {};

    constructor (private userState: UserState,
                 private walletResourceService: WalletResourceService,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletResourceService.defaultHeaders = userState.getExtraHeaders();

        //this.walletService.getAddresses(false);
    }

    public createAddress (coin) {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.selectedCoin = coin;
            this.walletResourceService.createAddressUsingGET(coin.name).subscribe(
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
