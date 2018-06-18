import {Component, ViewChild, Input, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletServiceStore } from '../../walletService.service';
import { TransactionsService } from '../../transactions/transactions.service';

import 'style-loader!./serverInfo.scss';

import * as b2bcoinModels from '../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletService } from '../../../services/com.b2beyond.api.b2bcoin';


@Component({
    selector: 'server-info',
    templateUrl: './serverInfo.html'
})
export class ServerInfo implements OnInit {

    @Input('coin')
    public coin : b2bcoinModels.WalletCoin;

    constructor (private userState: UserState,
                 private WalletService: WalletService,
                 private walletService: WalletServiceStore,
                 private transactionsService: TransactionsService,
                 private router: Router) {
    }

    ngOnInit(): void {
        this.WalletService.defaultHeaders = this.userState.getExtraHeaders(this.WalletService.defaultHeaders);
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

}
