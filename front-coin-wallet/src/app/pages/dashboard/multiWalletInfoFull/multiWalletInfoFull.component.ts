import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {ModalDirective} from 'ngx-bootstrap';

import {UserState} from '../../../user.state';
import {WalletServiceStore} from '../../walletService.service';

import 'style-loader!./multiWalletInfoFull.scss';
import {WalletService} from '../../../services/com.b2beyond.api.b2bcoin';


@Component({
    selector: 'multi-wallet-info-full',
    templateUrl: './multiWalletInfoFull.html'
})
export class MultiWalletInfoFull implements OnInit {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;


    public creatingWallet = false;


    constructor (private userState: UserState,
                 private WalletService: WalletService,
                 private walletService: WalletServiceStore,
                 private router: Router) {
    }

    public ngOnInit(): void {
        this.WalletService.defaultHeaders = this.userState.getExtraHeaders(this.WalletService.defaultHeaders);

        if (this.walletService.selectedCoin.name == "") {
            this.router.navigateByUrl("pages/dashboard/multiWallet");
        }
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.WalletService.createAddress(this.walletService.selectedCoin.name).subscribe(
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
        let numberOfAddresses = this.walletService.getNumberOfAddresses(this.walletService.selectedCoin);
        return {'value': numberOfAddresses };
    }

    public createNewPayment() {
        this.router.navigateByUrl("/pages/payments/create");
    }

}
