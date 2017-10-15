import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';
import { TransactionsService } from '../../transactions/transactions.service';

import 'style-loader!./serverInfo.scss';

import * as b2bcoinModels from '../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


@Component({
    selector: 'server-info',
    templateUrl: './serverInfo.html'
})
export class ServerInfo {

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

        this.initialize();
    }

    public getNumberOfCoinsInNetwork(): string {
        return (this.numberOfCoinsInNetwork / 1000000000000).toFixed(12) + " B2B";
    }

    public getCurrentBlockReward(): string {
        return (this.currentBlockReward / 1000000000000).toFixed(12) + " B2B";
    }

    public getLastBlockHash (): string {
        return this.lastBlockHash;
    }

    public gotoLastBlock() {
        this.transactionsService.searchString = this.lastBlockHash;
        console.log("Starting blockchain search", this.transactionsService.searchString);
        if (this.lastBlockHash != "") {
            this.router.navigateByUrl("pages/transactions/result");
            this.transactionsService.triggerSearch();
        }
    }

    private initialize () {
        console.log("Initialize ServerInfo");
        this.walletService.getLastBlockObservable().subscribe(result => {
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
                this.userState.handleError(error, this.initialize, this);
            }
        });
    }

}
