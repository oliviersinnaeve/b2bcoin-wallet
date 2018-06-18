import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {ModalDirective} from 'ngx-bootstrap';

import {UserState} from '../../../user.state';
import {WalletServiceStore} from '../../walletService.service';

import 'style-loader!./walletInfoFull.scss';
import {WalletService} from '../../../services/com.b2beyond.api.b2bcoin';


@Component({
    selector: 'wallet-info-full',
    templateUrl: './walletInfoFull.html'
})
export class WalletInfoFull implements OnInit {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;


    public creatingWallet = false;


    constructor (private userState: UserState,
                 private WalletService: WalletService,
                 private walletService: WalletServiceStore,
                 private router: Router) {
    }

    ngOnInit(): void {
        this.WalletService.defaultHeaders = this.userState.getExtraHeaders(this.WalletService.defaultHeaders);
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.WalletService.createAddress(this.walletService.primaryCoin.name).subscribe(
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
