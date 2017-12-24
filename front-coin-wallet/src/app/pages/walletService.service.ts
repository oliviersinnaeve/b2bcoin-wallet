import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router } from "@angular/router";

import { UserState } from '../user.state';

import * as b2bcoinModels from '../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../services/com.b2beyond.api.b2bcoin/api/WalletApi';

@Injectable()
export class WalletService {

    public addresses: Array<b2bcoinModels.UserAddress> = [];
    public coins: Array<b2bcoinModels.WalletCoin> = [];
    public primaryCoin: b2bcoinModels.WalletCoin = {name: ""} ;
    public selectedCoin: b2bcoinModels.WalletCoin = {name: ""} ;

    public serverInfos = {};

    public balancesBusy: boolean = false;
    public addressBalances = {};
    public transactionsBusy: boolean = false;
    public addressTransactions = {};
    public addressPayments = {};


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.getCoinTypes();

        let t = this;
        let interval = setInterval(function() {
            t.getServerInfo();
            t.getAddresses(true);
        }, 45000);
    }

    public getCoinTypes () {
        if (this.coins == undefined || this.coins.length == 0) {
            this.walletApi.getCoinTypes().subscribe(result => {
                    this.coins = result;
                    this.setPrimaryCoin();
                    this.getServerInfo();
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
            this.walletApi.getTransactionsForAddress(this.addresses[i].currency.name, this.addresses[i]).subscribe(result => {
                    //console.log("Transactions result", result);

                    let found: boolean = false;
                    if (this.addressTransactions[result.currencyName]) {
                        for (var j = 0; j < this.addressTransactions[result.currencyName].length; j++) {
                            if (result.address == this.addressTransactions[result.currencyName][j].address
                                || result.address == this.addressPayments[result.currencyName][j].address) {
                                found = true;
                            }
                        }
                    }

                    let newTransactions = {
                        address: result.address,
                        transactions: []
                    };
                    let newTransactionsPayments = {
                        address: result.address,
                        transactions: []
                    };

                    for (let i = 0; i < result.items.length; i++) {
                        if (result.items[i].transactions.length > 0 && result.items[i].transactions[0].amount < 0) {
                            newTransactionsPayments.transactions.push(result.items[i]);
                        } else {
                            newTransactions.transactions.push(result.items[i]);
                        }
                    }

                    if (!found) {
                        if (!this.addressTransactions[result.currencyName]) {
                            this.addressTransactions[result.currencyName] = [];
                        }
                        this.addressTransactions[result.currencyName].push(newTransactions);

                        if (!this.addressPayments[result.currencyName]) {
                            this.addressPayments[result.currencyName] = [];
                        }
                        this.addressPayments[result.currencyName].push(newTransactionsPayments);
                    }

                    count -= 1;
                    if (count == 0) {
                        //console.log("Setting transactions busy on false !!!!");
                        this.transactionsBusy = false;
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
        return this.addressTransactions[coin.name];
    }

    public getPaymentsForCoin(coin) {
        return this.addressPayments[coin.name];
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

    public getAmount (amount: number, coinType): string {
        if (amount !== undefined) {
            if (coinType && coinType.toUpperCase() == "BTC") {
                return (amount).toFixed(8) + " " + coinType.toUpperCase();
            } else if (coinType) {
                return (amount).toFixed(12) + " " + coinType.toUpperCase();
            }
        }
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
