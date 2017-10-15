import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';


import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import { TransactionsService } from '../../transactions.service';


@Component({
    selector: 'single-result',
    templateUrl: './singleResult.html',
})

export class SingleResult implements OnInit {

    public result : b2bcoinModels.BlockOrTransactionResponse = null;

    public searching: boolean = false;


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private transactionService: TransactionsService,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

    }

    public ngOnInit() {
        this.transactionService.searchUpdated.subscribe(
            (result) => {
                console.log("search text was updated", result);
                this.search(undefined);
            }
        );
        this.search(undefined);
    }

    private search(searchString: string) {
        this.searching = true;
        if (searchString != undefined) {
            this.transactionService.searchString = searchString;
        }
        console.log("In transaction service init with searchString", this.transactionService.searchString);
        this.walletApi.getBlockOrTransaction({"hash": this.transactionService.searchString}).subscribe(result => {
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

    public isBlockFetched(): boolean {
        console.log("checking block wrapper", this.result);
        let isResult = this.result != null;
        console.log("return value block fetched - result", isResult);
        if (isResult) {
            let isBlock = this.result.blockWrapper != null;
            console.log("return value block fetched - block wrapper", isBlock);

            if (isBlock) {
                let isHash = this.result.blockWrapper.block.hash != undefined;
                console.log("return value block fetched - block hash", isHash);

                return isHash;
            }
        }

        return false;
    }

    public isTransactionFetched(): boolean {
        console.log("checking transaction wrapper", this.result);
        console.log("return value transaction fetched", this.result != null && this.result.transactionWrapper != null);
        return this.result != null && this.result.transactionWrapper != null;
    }

    public getParsedAmount(amount: number): string {
        if (amount != undefined && amount != null) {
            return (amount / 1000000000000).toFixed(12) + " B2B";
        } else {
            return (0 / 1000000000000).toFixed(12) + " B2B";
        }
    }

    public getDate(timestamp : number): string {
        console.log("time to parse", timestamp);
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
            return (result / 1000000000000).toFixed(12) + " B2B";
        } else {
            return (0 / 1000000000000).toFixed(12) + " B2B";
        }
    }

}
