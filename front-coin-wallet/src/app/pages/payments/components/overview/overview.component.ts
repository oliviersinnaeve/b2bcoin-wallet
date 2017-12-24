import { Component, ViewChild, OnInit, Input } from '@angular/core';
import { Router } from "@angular/router";
import { Http, Headers, RequestOptionsArgs, Response, URLSearchParams } from '@angular/http';
import { ModalDirective } from 'ngx-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { LocalDataSource } from 'ng2-smart-table';

import { UserState } from '../../../../user.state';

import { WalletService } from '../../../walletService.service';
import { TransactionsService } from '../../../transactions/transactions.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import { websiteId } from '../../../../environment';

import { PagerService } from '../../../../services/pager.service'

@Component({
    selector: 'payment-overview',
    styleUrls: ['overview.scss'],
    templateUrl: './overview.html',
})

export class PaymentOverview implements OnInit {

    @Input('coin')
    public coin: b2bcoinModels.WalletCoin;

    public transactions = [];

    // pager object
    public pager: any = {
        pages: []
    };

    // paged items
    public pagedItems: any[] = [];


    constructor (private notificationsService: NotificationsService,
                 private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private pagerService: PagerService,
                 private transactionsService: TransactionsService,
                 private http: Http,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.getTransactions();
    }


    public getDate(transactionWrapper: any): string {
        //console.log("Using trasnaxcitonWrapper", transactionWrapper);
        let date = new Date(transactionWrapper.transactions[0].timestamp * 1000);
        let minutes = (date.getMinutes() < 10)? "0" + date.getMinutes(): date.getMinutes();
        let hours = (date.getHours() < 10)? "0" + date.getHours(): date.getHours();
        let month = (date.getMonth() < 10)? "0" + date.getMonth(): date.getMonth();
        let day = (date.getDate() < 10)? "0" + date.getDate(): date.getDate();
        return day + "-" + month + "-" + date.getFullYear() + " " + hours + ":" + minutes;
    }

    public getFee(transactionWrapper: any): string {
        //console.log("Using trasnaxcitonWrapper", transactionWrapper);
        return (transactionWrapper.transactions[0].fee / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
    }

    public getAmount(transactionWrapper: any): string {
        return (transactionWrapper.transactions[0].amount / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
    }

    public getTransactions() {
        let t = this;
        let interval = setInterval(function() {
            if (!t.walletService.transactionsBusy && t.walletService.getPaymentsForCoin(t.coin) != undefined) {
                let transactionsAddresses = t.walletService.getPaymentsForCoin(t.coin);

                //console.log("Setting transactions", transactionsAddresses);

                let transactions = [];
                for (let i = 0; i < transactionsAddresses.length; i++) {
                    for (let j = transactionsAddresses[i].transactions.length; j > 0; j--) {
                        if (transactionsAddresses[i].transactions[j] != undefined) {
                            transactions.push(transactionsAddresses[i].transactions[j]);
                        }
                    }
                }

                t.transactions = transactions;
                t.setPage(1);
                clearInterval(interval);
            }
        }, 250);
    }

    public ngOnInit(): void {
        this.getTransactions();
    }

    setPage(page: number) {
        //console.log("Building pages", this.pager);
        if (page < 1 || page > this.pager.totalPages) {
            return;
        }

        // get pager object from service
        this.pager = this.pagerService.getPager(this.transactions.length, page);
        // get current page of items
        this.pagedItems = this.transactions.slice(this.pager.startIndex, this.pager.endIndex + 1);
        //console.log("Building pages DONE", this.pager);
    }

    public gotoBlock(hash) {
        this.transactionsService.searchString = hash;
        this.transactionsService.coin = this.coin;
        //console.log("Starting blockchain search", this.transactionsService.searchString);
        if (this.transactionsService.searchString != undefined && this.transactionsService.searchString != "") {
            this.router.navigateByUrl("pages/explorer");
            this.transactionsService.triggerSearch();
        }
    }

}
