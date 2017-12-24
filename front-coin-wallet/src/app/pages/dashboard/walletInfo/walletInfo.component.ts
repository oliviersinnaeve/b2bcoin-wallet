import { Component, ViewChild, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';
import { TransactionsService } from '../../transactions/transactions.service';

import * as Chart from 'chart.js';

import 'style-loader!./walletInfo.scss';

import * as b2bcoinModels from '../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


@Component({
    selector: 'wallet-info',
    templateUrl: './walletInfo.html'
})
export class WalletInfo implements OnInit {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    @Input('coin')
    public coin : b2bcoinModels.WalletCoin;

    // Wallet info
    public numberOfAddresses: number;
    public balance: number = 0;
    public lockedBalance: number = 0;
    public creatingWallet = false;

    // Server info
    public serverError: boolean = false;
    public lastBlockHash: string;
    public numberOfCoinsInNetwork: number = 0;
    public difficulty: number = 0;
    public currentBlockReward: number = 0;
    public currentBlockHeight: number = 0;

    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private transactionsService: TransactionsService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.walletService.getAddresses();
    }

    public getNumberOfCoinsInNetwork(): string {
        return (this.numberOfCoinsInNetwork / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
    }

    public getCurrentBlockReward(): string {
        return (this.currentBlockReward / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
    }

    public getLastBlockHash (): string {
        return this.lastBlockHash;
    }

    public gotoLastBlock() {
        this.transactionsService.searchString = this.lastBlockHash;
        this.transactionsService.coin = this.coin;
        //console.log("Starting blockchain search", this.transactionsService.searchString);
        if (this.lastBlockHash != "") {
            this.router.navigateByUrl("pages/explorer");
            this.transactionsService.triggerSearch();
        }
    }

    public createNewAddress () {
        if (!this.creatingWallet) {
            this.creatingWallet = true;
            this.walletApi.createAddress(this.coin.name).subscribe(
                    result => {
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
        let numberOfAddresses = this.walletService.getNumberOfAddresses(this.coin);
        return {'value': numberOfAddresses };
    }

    public ngOnInit(): void {
        this.walletService.getLastBlockObservable(this.coin.name).subscribe(result => {
                this.serverError = false;
                console.log("Result fetched", result);
                this.lastBlockHash = result.block.hash;
                this.numberOfCoinsInNetwork = result.block.alreadyGeneratedCoins;
                this.difficulty = result.block.difficulty;
                this.currentBlockReward = result.block.baseReward;
                this.currentBlockHeight = result.block.height;
            },
            (error) => {
                console.log("Error fetched", error);
                if (error.status === 401) {
                    this.userState.handleError(error, this.ngOnInit, this);
                }
                if (error.status === 404) {
                    this.serverError = true;
                }
            });
    }

}
