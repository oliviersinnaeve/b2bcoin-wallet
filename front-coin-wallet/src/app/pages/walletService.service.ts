import { Injectable, EventEmitter, Output } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router } from "@angular/router";

import { UserState } from '../user.state';

import * as b2bcoinModels from '../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../services/com.b2beyond.api.b2bcoin/api/WalletApi';
import { FaucetApi } from '../services/com.b2beyond.api.b2bcoin/api/FaucetApi';

import { websiteId } from '../environment';

@Injectable()
export class WalletService {

    public addresses: Array<b2bcoinModels.UserAddress> = [];
    public coins: Array<b2bcoinModels.WalletCoin> = [];
    public primaryCoin: b2bcoinModels.WalletCoin = {name: ""} ;
    public selectedCoin: b2bcoinModels.WalletCoin = {name: ""} ;

    public faucetAddress: b2bcoinModels.UserAddress = {};
    public faucetAddressPayments: Array<b2bcoinModels.FaucetAddressPayment> = [];
    public hasFaucetAddress: boolean = false;

    public serverInfos = {};

    public balancesBusy: boolean = false;
    public addressBalances = {};
    public transactionsBusy: boolean = false;
    public addressTransactions = {};
    public addressPayments = {};

    @Output()
    public addressFetchedEmitter = new EventEmitter<b2bcoinModels.UserAddress>();

    @Output()
    public faucetAddressUpdateEmitter = new EventEmitter<b2bcoinModels.UserAddress>();

    @Output()
    public faucetAddressPaymentsFetchedEmitter = new EventEmitter<Array<b2bcoinModels.FaucetAddressPayment>>();

    @Output()
    public transactionsFetchedEmitter = new EventEmitter<b2bcoinModels.WalletCoin>();

    @Output()
    public paymentsFetchedEmitter = new EventEmitter<b2bcoinModels.WalletCoin>();


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private faucetApi: FaucetApi,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();
        this.faucetApi.defaultHeaders = userState.getExtraHeaders();

        this.getCoinTypes();

        let t = this;
        let interval = setInterval(function() {
            t.getCoinTypes();
        }, 120000);
    }

    public getCoinTypes () {
        if (this.coins == undefined || this.coins.length == 0) {
            this.walletApi.getCoinTypes().subscribe(result => {
                    this.coins = result;

                    this.getAddresses(true);

                    this.setPrimaryCoin();
                    this.getFaucetAddress();
                    this.getServerInfo();

                    this.addressFetchedEmitter.emit(this.primaryCoin);
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getCoinTypes, this);
                }
            });
        }

        return this.coins;
    }

    public getServerInfo() {
        for (let i = 0; i < this.coins.length; i++) {
            this.getLastBlockObservable(this.coins[i].name).subscribe(result => {
                    //console.log("Result fetched", result);

                    this.serverInfos[result.currencyName] = result;
                },
                (error) => {
                    console.log("Error fetched", error);
                    if (error.status === 401) {
                        this.userState.handleError(error, this.getServerInfo, this);
                    }
                });
        }
    }

    public setPrimaryCoin () {
        //console.log("using coins", this.coins);
        for (let i = 0; i < this.coins.length; i++) {
            if (this.coins[i].primaryCoin) {
                //console.log("set primary coin", this.coins[i]);
                this.primaryCoin = this.coins[i];
            }
        }
    }

    public getAddresses (force) {
        if (this.addresses.length == 0 || force) {
            this.walletApi.getAddresses().subscribe(result => {
                this.addresses = result;
                if (!this.balancesBusy) {
                    this.balancesBusy = true;
                    this.getBalances();
                }
                if (!this.transactionsBusy) {
                    this.transactionsBusy = true;
                    this.getTransactions();
                }

                return this.addresses;
                },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getAddresses, this);
                }
            });
        }

        return this.addresses;
    }

    public hasAddresses(coin): boolean {
        return this.getNumberOfAddresses(coin) > 0;
    }

    public getNumberOfAddresses (coinType : b2bcoinModels.WalletCoin) {
        let numberOfAddresses = 0;
        for (let i = 0; i < this.addresses.length; i++) {
            if (this.addresses[i].currency.name == coinType.name) {
                numberOfAddresses += 1;
            }
        }
        return numberOfAddresses;
    }

    public getAddressesForCoin(coinType : b2bcoinModels.WalletCoin) {
        let addressesForCoin = [];
        for (let i = 0; i < this.addresses.length; i++) {
            if (this.addresses[i].currency.name == coinType.name) {
                addressesForCoin.push(this.addresses[i]);
            }
        }
        return addressesForCoin;
    }

    public getBalances() {
        let count = this.addresses.length;

        for (var i = 0; i < this.addresses.length; i++) {
            this.getBalanceObservable({
                address: this.addresses[i].address,
                currency: this.addresses[i].currency
            }).subscribe((result) => {
                    //console.log("Address balance in add coins view", result);
                    for (var j = 0; j < this.addresses.length; j++) {
                        var address = this.addresses[j];

                        if (address.address == result.address) {
                            if (this.addressBalances[address.currency.name] == undefined) {
                                this.addressBalances[address.currency.name] = [];
                            }

                            let found: boolean = false;
                            for (var i = 0; i < this.addressBalances[address.currency.name].length; i++) {
                                if (result.address == this.addressBalances[address.currency.name][i].address) {
                                    found = true;
                                }
                            }

                            if (!found) {
                                this.addressBalances[address.currency.name].push(result);
                            }
                        }
                    }

                    count -= 1;
                    if (count == 0) {
                        //console.log("Setting balance busy on false !!!!");
                        this.balancesBusy = false;
                    }
                },
                (error) => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.getBalances, this);
                    } else {
                        count -= 1;
                    }
                }
            );
            //}
        }
    }

    public getTransactions() {
        let count = this.addresses.length;

        for (var i = 0; i < this.addresses.length; i++) {
            if (!this.addressTransactions[this.addresses[i].currency.name]) {
                this.addressTransactions[this.addresses[i].currency.name] = [];
                this.addressPayments[this.addresses[i].currency.name] = [];
            }
        }

        for (var i = 0; i < this.addresses.length; i++) {
            this.walletApi.getTransactionsForAddress(this.addresses[i].currency.name, this.addresses[i]).subscribe(result => {
                    console.log("Transactions result", result);

                    result.transactionResponses.forEach(function(element) {
                        if (element.amount > 0) {
                            this.addressTransactions[result.currencyName].push(element);
                        } else {
                            this.addressPayments[result.currencyName].push(element);
                        }
                    }, this);

                    count -= 1;
                    if (count == 0) {
                        //console.log("Setting transactions busy on false !!!!");
                        this.transactionsBusy = false;
                        this.paymentsFetchedEmitter.emit(this.getCoinForName(result.currencyName));
                        this.transactionsFetchedEmitter.emit(this.getCoinForName(result.currencyName));
                    }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getTransactions, this);
                } else {
                    count -= 1;
                }
            });
        }
    }

    public getTransactionsForCoin(coin) {
        let result = this.addressTransactions[coin.name];
        return (result != null && result != undefined)? result: [];
    }

    public getPaymentsForCoin(coin) {
        let result = this.addressPayments[coin.name];
        return (result != null && result != undefined)? result: [];
    }

    public getConvertedAmount (coin, amount): string {
        if (amount) {
            return (amount / coin.convertAmount).toFixed(coin.fractionDigits);
        } else {
            return (0 / coin.convertAmount).toFixed(coin.fractionDigits);
        }
    }


    public getBalance (coin): string {
        if (this.addressBalances[coin.name]) {
            let availableBalance = 0;
            for (let i = 0; i < this.addressBalances[coin.name].length; i++) {
                availableBalance += this.addressBalances[coin.name][i].availableBalance;
            }
            return (availableBalance / coin.convertAmount).toFixed(coin.fractionDigits);
        } else {
            return (0 / coin.convertAmount).toFixed(coin.fractionDigits);
            //return "0.000000000000";
        }
    }

    public getSpendKeysObservable(coin, address: string) {
        let userAddress : b2bcoinModels.UserAddress = {};
        userAddress.address = address;
        userAddress.currency = coin;
        return this.walletApi.getSpendKeys(userAddress);
    }

    public getLockedBalance (coin): string {
        if (this.addressBalances[coin.name]) {
            let lockedAmount = 0;
            for (let i = 0; i < this.addressBalances[coin.name].length; i++) {
                lockedAmount += this.addressBalances[coin.name][i].lockedAmount;
            }
            return (lockedAmount / coin.convertAmount).toFixed(coin.fractionDigits);
        } else {
            return (0 / coin.convertAmount).toFixed(coin.fractionDigits);
            //return "0.000000000000";
        }
    }

    public getBalanceForAddress (coin, address): string {
        if (this.addressBalances[coin.name]) {
            let availableBalance = 0;
            for (let i = 0; i < this.addressBalances[coin.name].length; i++) {
                if (this.addressBalances[coin.name][i].address == address.address) {
                    availableBalance += this.addressBalances[coin.name][i].availableBalance;
                }
            }
            return (availableBalance / coin.convertAmount).toFixed(coin.fractionDigits);
        } else {
            return (0 / coin.convertAmount).toFixed(coin.fractionDigits);
            //return "0.000000000000";
        }
    }

    public getLockedBalanceForAddress (coin, address): string {
        if (this.addressBalances[coin.name]) {
            let lockedAmount = 0;
            for (let i = 0; i < this.addressBalances[coin.name].length; i++) {
                if (this.addressBalances[coin.name][i].address == address.address) {
                    lockedAmount += this.addressBalances[coin.name][i].lockedAmount;
                }
            }
            return (lockedAmount / coin.convertAmount).toFixed(coin.fractionDigits);
        } else {
            return (0 / coin.convertAmount).toFixed(coin.fractionDigits);
            //return "0.000000000000";
        }
    }

    public getAddressesObservable (): Observable<Array<b2bcoinModels.Address>> {
        return this.walletApi.getAddresses();
    }

    public getBalanceObservable (userAddress: b2bcoinModels.UserAddress): Observable<b2bcoinModels.AddressBalance> {
        return this.walletApi.getBalance(userAddress);
    }

    public getLastBlockObservable (coinType: string): Observable<b2bcoinModels.BlockWrapper> {
        //console.log("Getting last block for coin", coinType);
        return this.walletApi.getLastBlock(coinType);
    }

    //public getAmount (amount: number, coinType): string {
    //    if (amount !== undefined) {
    //        if (coinType && coinType.toUpperCase() == "BTC") {
    //            return (amount).toFixed(8) + " " + coinType.toUpperCase();
    //        } else if (coinType) {
    //            return (amount).toFixed(12) + " " + coinType.toUpperCase();
    //        }
    //    }
    //}

    public getFaucetAddress() {
        console.log("Fetching faucet");
        let faucetAddressRequest = {
            websiteId: websiteId,
            coin: this.primaryCoin
        };

        return this.faucetApi.getFaucetAddress(false, faucetAddressRequest).subscribe(
            (result) => {
                console.log("Faucet address received", result);
                if (result) {
                    this.faucetAddress = result;
                    this.hasFaucetAddress = true;

                    this.getFaucetAddressPayments(this.faucetAddress);
                }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getFaucetAddress, this);
                }
            }
        );
    }

    public updateFaucetAddress() {
        console.log("Update faucet");

        return this.faucetApi.updateFaucetAddress(this.faucetAddress).subscribe(
            (result) => {
                console.log("Faucet address updated", result);
                if (result) {
                    this.faucetAddressUpdateEmitter.emit(this.faucetAddress);
                }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.updateFaucetAddress, this);
                }
            }
        );
    }

    public getFaucetAddressPayments(address) {
        return this.faucetApi.getFaucetPayments(address).subscribe(
            (result) => {
                console.log("Faucet address payments received", result);
                if (result) {
                    this.faucetAddressPayments = result;
                    this.faucetAddressPaymentsFetchedEmitter.emit(this.faucetAddressPayments);
                }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.getFaucetAddressPayments, this);
                }
            }
        );
    }

    private getCoinForName(coinName): b2bcoinModels.WalletCoin {
        for (let i = 0; i < this.coins.length; i++) {
            if (this.coins[i].name == coinName) {
                return this.coins[i];
            }
        }

        return undefined;
    }

    // NAVIGATIONS
    public showCoinFull(coin) {
        this.selectedCoin = coin;
        if (this.selectedCoin.primaryCoin) {
            this.router.navigateByUrl("pages/dashboard/mainWallet");
        } else {
            this.router.navigateByUrl("pages/dashboard/multiWalletInfo");
        }
    }

}
