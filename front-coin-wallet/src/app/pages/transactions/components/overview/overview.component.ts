import { Component, ViewChild, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Http, Headers, RequestOptionsArgs, Response, URLSearchParams } from '@angular/http';
import { ModalDirective } from 'ngx-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { UserState } from '../../../../user.state';
import { WalletService } from '../../../walletService.service';
import { TransactionsService } from '../../transactions.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import { websiteId } from '../../../../environment';

@Component({
    selector: 'overview',
    templateUrl: './overview.html',
})

export class Overview implements OnInit {

    public transactions: Array<any> = [];

    constructor (private notificationsService: NotificationsService,
                 private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private transactionsService: TransactionsService,
                 private http: Http,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }

    public ngOnInit (): void {
        this.walletService.getAddressesObservable().subscribe(result => {
                for (var i = 0; i < result.length; i++) {
                    let address: b2bcoinModels.Address = {};
                    address.address = result[i].address;
                    this.walletApi.getTransactionsForAddress(address).subscribe(result => {
                            console.log("Transactions result", result);
                            let newTransactions = {
                                address: address.address,
                                transactions: []
                            };
                            this.transactions.push(newTransactions);

                            for (let i = 0; i < result.items.length; i++) {
                                if (result.items[i].transactions.length > 0) {
                                    newTransactions.transactions.push(result.items[i]);
                                }
                            }
                        },
                        (error) => {
                            if (error.status === 401) {
                                this.userState.handleError(error, this.ngOnInit, this);
                            }
                        });
                }
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.ngOnInit, this);
                }
            });
    }

    public gotoTransaction(hash) {
        this.transactionsService.searchString = hash;
        console.log("Starting blockchain search", this.transactionsService.searchString);
        if (this.blockHash != "") {
            this.router.navigateByUrl("pages/transactions/result");
            this.transactionsService.triggerSearch();
        }
    }

    public getFee(transactionWrapper: any): string {
        console.log("Using trasnaxcitonWrapper", transactionWrapper);
        return (transactionWrapper.transactions[0].fee / 1000000000000).toFixed(12) + " B2B";
    }

    public getAmount(transactionWrapper: any): string {
        return (transactionWrapper.transactions[0].amount / 1000000000000).toFixed(12) + " B2B";
    }
}
