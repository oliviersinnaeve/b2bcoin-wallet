import { Component, ViewChild, Input } from '@angular/core';
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

    @Input('coin')
    public coin : b2bcoinModels.WalletCoin;

    //public lastBlockHash: string;
    //public numberOfCoinsInNetwork: number = 0;
    //public difficulty: number = 0;
    //public currentBlockReward: number = 0;
    //public currentBlockHeight: number = 0;


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private transactionsService: TransactionsService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }

    public getNumberOfCoinsInNetwork(): string {
        if (this.walletService.serverInfos[this.coin.name]) {
            return (this.walletService.serverInfos[this.coin.name].block.alreadyGeneratedCoins / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
        } else {
            return "";
        }
    }

    public getCurrentBlockReward(): string {
        if (this.walletService.serverInfos[this.coin.name]) {
            return (this.walletService.serverInfos[this.coin.name].block.reward / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
        } else {
            return "";
        }
    }

    public getCurrentBlockHeight(): string {
        if (this.walletService.serverInfos[this.coin.name]) {
            return this.walletService.serverInfos[this.coin.name].block.height;
        } else {
            return "";
        }
    }

    public getDifficulty(): string {
        if (this.walletService.serverInfos[this.coin.name]) {
            return this.walletService.serverInfos[this.coin.name].block.difficulty;
        } else {
            return "";
        }
    }

    public getLastBlockHash (): string {
        if (this.walletService.serverInfos[this.coin.name]) {
            return this.walletService.serverInfos[this.coin.name].block.hash;
        } else {
            return "";
        }
    }

    public gotoLastBlock() {
        this.transactionsService.searchString = this.walletService.serverInfos[this.coin.name].block.hash;
        this.transactionsService.coin = this.coin;
        console.log("Starting blockchain search", this.transactionsService.searchString);
        if (this.transactionsService.searchString != undefined && this.transactionsService.searchString != "") {
            this.router.navigateByUrl("pages/explorer");
            this.transactionsService.triggerSearch();
        }
    }

    //public ngOnInit (): void {
    //    if (this.coin && this.coin.name) {
    //        console.log("Initialize ServerInfo - serverInfo", this.coin.name);
    //        this.walletService.getLastBlockObservable(this.coin.name).subscribe(result => {
    //                console.log("Result fetched", result);
    //                this.lastBlockHash = result.block.hash;
    //                this.numberOfCoinsInNetwork = result.block.alreadyGeneratedCoins;
    //                this.difficulty = result.block.difficulty;
    //                this.currentBlockReward = result.block.baseReward;
    //                this.currentBlockHeight = result.block.height;
    //            },
    //            (error) => {
    //                console.log("Error fetched", error);
    //                if (error.status === 401) {
    //                    this.userState.handleError(error, this.ngOnInit, this);
    //                }
    //            });
    //    }
    //}

}
