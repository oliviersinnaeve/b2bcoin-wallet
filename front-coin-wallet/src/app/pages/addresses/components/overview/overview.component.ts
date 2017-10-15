import { Component, ViewChild } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletService } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import 'rxjs/Rx';

@Component({
    selector: 'overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class Overview {

    @ViewChild('confirmDeleteAddressModal') confirmDeleteAddressModal: ModalDirective;
    @ViewChild('deleteAddressModal') deleteAddressModal: ModalDirective;


    public addresses: Array<b2bcoinModels.AddressBalance> = [];

    private addressToDelete : b2bcoinModels.AddressBalance;

    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();
        this.initialize();
    }

    public getBalance (address): string {
        console.log("Get balance for ", address);
        if (address.availableBalance) {
            return (parseFloat(address.availableBalance) / 1000000000000).toFixed(12);
        } else {
            return "0.000000000000";
        }
    }

    public getLockedBalance (address): string {
        console.log("Get locked balance for ", address);
        if (address.balance) {
            return (parseFloat(address.lockedAmount) / 1000000000000).toFixed(12);
        } else {
            return "0.000000000000";
        }
    }

    public deleteAddress () {
        this.confirmDeleteAddressModal.hide();

        this.walletApi.deleteAddress({ address: this.addressToDelete.address }).subscribe(
                result => {
                this.deleteAddressModal.show();
                for (let i = 0; i < this.addresses.length; i++) {
                    this.initialize();
                    this.addressToDelete = undefined;
                }
            },
                error => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.deleteAddress, this);
                }
            }
        );
    }

    public setDeleteAddress(item) {
        this.addressToDelete = item;
        this.confirmDeleteAddressModal.show();
    }

    private initialize () {
        console.log("Getting overview websites");
        this.walletApi.getAddresses().subscribe(
            (result) => {
                console.log(result);
                this.addresses = result;

                for (var i = 0; i < this.addresses.length; i++) {
                    var address = this.addresses[i];
                    this.walletService.getBalanceObservable(address.address).subscribe(
                        (result) => {
                            for (var j = 0; j < this.addresses.length; j++) {
                                var oldAddress = this.addresses[j];
                                if (oldAddress.address == result.address) {
                                    console.log("Setting balance", result, oldAddress);
                                    this.addresses[j].availableBalance  = result.availableBalance;
                                    this.addresses[j].lockedAmount  = result.lockedAmount;
                                }
                            }
                        },
                        (error) => {
                            if (error.status === 401) {
                                this.userState.handleError(error, this.getBalance, this);
                            }
                        }
                    );
                }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.initialize, this);
                }
            }
        );
    }

}
