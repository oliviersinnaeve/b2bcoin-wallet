import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserState} from '../../../../user.state';


import * as b2bcoinModels from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin/model/models';
import {WalletResourceService} from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin/';

import {TransactionsService} from '../../../transactions/transactions.service';
import {WalletService} from '../../../walletService.service';


@Component({
    selector: 'explorer-result',
    styleUrls: ["overview.scss"],
    templateUrl: './overview.html',
})

export class ExplorerResult implements OnInit {

    public result: b2bcoinModels.BlockOrTransactionResponse = null;

    public searching: boolean = false;


    constructor(private userState: UserState,
                private walletApi: WalletResourceService,
                private transactionService: TransactionsService,
                private walletService: WalletService,
                private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        if (this.transactionService.coin == undefined) {
            this.transactionService.coin = this.walletService.primaryCoin;
        }
    }

    public ngOnInit() {
        if (this.transactionService.coin == undefined) {
            this.transactionService.coin = this.walletService.primaryCoin;
        }

        this.transactionService.searchUpdated.subscribe(
            (result) => {
                //console.log("search text was updated", result);
                this.search(undefined);
            }
        );
        this.search(undefined);
    }

    public search(searchString: string) {
        this.searching = true;
        if (searchString != undefined) {
            this.transactionService.searchString = searchString;
        }

        if (this.transactionService.coin != undefined) {
            //console.log("In transaction service init with searchString", this.transactionService.searchString);
            //console.log("In transaction service init with coin", this.transactionService.coin);
            this.walletApi.getBlockOrTransactionUsingPOST(this.transactionService.coin.name, {"hash": this.transactionService.searchString}).subscribe(result => {
                    console.log("Setting result", result);
                    this.result = result;
                    if (this.result.blockWrapper == undefined) {
                        this.result.blockWrapper = {
                            block: {
                                transactions: []
                            }
                        };
                    }
                    this.searching = false;
                },
                error => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.ngOnInit, this);
                    }
                });
        }
    }

    public isBlockFetched(): boolean {
        //console.log("checking block wrapper", this.result);
        let isResult = this.result != null;
        //console.log("return value block fetched - result", isResult);
        if (isResult) {
            let isBlock = this.result.blockWrapper != null;
            //console.log("return value block fetched - block wrapper", isBlock);

            if (isBlock) {
                let isHash = this.result.blockWrapper.block.hash != undefined;
                //console.log("return value block fetched - block hash", isHash);

                return isHash;
            }
        }

        return false;
    }

    public isTransactionFetched(): boolean {
        //console.log("checking transaction wrapper", this.result);
        //console.log("return value transaction fetched", this.result != null && this.result.transactionWrapper != null);
        return this.result != null && this.result.transactionWrapper != null;
    }

    public getParsedAmount(amount: number): string {
        if (amount != undefined && amount != null) {
            return (amount / this.transactionService.coin.convertAmount).toFixed(this.transactionService.coin.fractionDigits) + " " + this.transactionService.coin.name;
        } else {
            return (0 / this.transactionService.coin.convertAmount).toFixed(this.transactionService.coin.fractionDigits) + " " + this.transactionService.coin.name;
        }
    }

    public getDate(timestamp: number): string {
        //console.log("time to parse", timestamp);
        if (timestamp != undefined && timestamp != null) {
            return new Date(timestamp * 1000).toDateString();
        }

        return "";
    }

    public getBlockTransactionFees() {
        if (this.result.blockWrapper != null && this.result.blockWrapper.block.transactions.length > 0) {
            let result = 0;
            for (let i = 0; i < this.result.blockWrapper.block.transactions.length; i++) {
                let transaction = this.result.blockWrapper.block.transactions[i];
                result += transaction.fee;
            }
            return (result / this.transactionService.coin.convertAmount).toFixed(this.transactionService.coin.fractionDigits) + " " + this.transactionService.coin.name;
        } else {
            return (0 / this.transactionService.coin.convertAmount).toFixed(this.transactionService.coin.fractionDigits) + " " + this.transactionService.coin.name;
        }
    }

}
