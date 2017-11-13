import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { UserState } from '../user.state';

import * as b2bcoinModels from '../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../services/com.b2beyond.api.b2bcoin/api/WalletApi';

@Injectable()
export class WalletService {

    public addresses: Array<b2bcoinModels.Address>;

    constructor (private userState: UserState,
                 private walletApi: WalletApi) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }

    public getAddresses () {
        this.walletApi.getAddresses().subscribe(result => {
                this.addresses = result;
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getAddresses, this);
                }
            });
    }

    public getAddressesObservable (): Observable<Array<b2bcoinModels.Address>> {
        return this.walletApi.getAddresses();
    }

    public getBalanceObservable (userAddress: b2bcoinModels.UserAddress): Observable<b2bcoinModels.AddressBalance> {
        return this.walletApi.getBalance(userAddress);
    }

    public getLastBlockObservable (): Observable<b2bcoinModels.BlockWrapper> {
        return this.walletApi.getLastBlock();
    }

    public getAmount (amount: number, coinType): string {
        if (amount !== undefined) {
            if (coinType && coinType.toUpperCase() == "BTC") {
                return (amount).toFixed(8) + " " + coinType.toUpperCase();
            } else if (coinType) {
                return (amount).toFixed(12) + " " + coinType.toUpperCase();
            }
        }
    }

}
