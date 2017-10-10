import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';

import { WalletService } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import 'rxjs/Rx';

@Component({
    selector: 'overview',
    templateUrl: './overview.html'
})

export class Overview {

    public addresses: Array<b2bcoinModels.AddressBalance> = [];

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
