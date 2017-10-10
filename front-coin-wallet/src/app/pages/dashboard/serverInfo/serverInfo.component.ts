import { Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { UserState } from '../../../user.state';
import { WalletService } from '../../walletService.service';

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


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.initialize();
    }

    public getNumberOfCoinsInNetwork(): string {
        return (this.numberOfCoinsInNetwork / 1000000000000).toFixed(12);
    }

    public getLastBlockHash (): string {
        return this.lastBlockHash;
    }

    private initialize () {
        console.log("Initialize ServerInfo");
        this.walletService.getLastBlockObservable().subscribe(result => {
                console.log("Result fetched", result);
            this.lastBlockHash = result.block.hash;
            this.numberOfCoinsInNetwork = result.block.alreadyGeneratedCoins;
        },
        (error) => {
            console.log("Error fetched", error);
            if (error.status === 401) {
                this.userState.handleError(error, this.initialize, this);
            }
        });
    }

}
