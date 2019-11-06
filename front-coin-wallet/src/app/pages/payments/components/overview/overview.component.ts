import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NotificationsService} from 'angular2-notifications';

import {UserState} from '../../../../user.state';

import {WalletService} from '../../../walletService.service';
import {TransactionsService} from '../../../transactions/transactions.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin/model/models';
import {WalletResourceService} from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin';

import {PagerService} from '../../../../services/pager.service'

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
                 private walletApi: WalletResourceService,
                 private walletService: WalletService,
                 private pagerService: PagerService,
                 private transactionsService: TransactionsService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }


    public ngOnInit(): void {
        if (this.walletService.getPaymentsForCoin(this.coin).length > 0 && !this.walletService.transactionsBusy) {
            this.getTransactions(this.walletService.getPaymentsForCoin(this.coin));
        } else {
            this.walletService.paymentsFetchedEmitter
                .subscribe(item => this.getTransactions(item));
        }
    }

    public getDate(transactionWrapper: any): string {
        //console.log("Using trasnaxcitonWrapper", transactionWrapper);
        let date = new Date(transactionWrapper.date * 1000);
        let minutes = (date.getMinutes() < 10)? "0" + date.getMinutes(): date.getMinutes();
        let hours = (date.getHours() < 10)? "0" + date.getHours(): date.getHours();
        let month = (date.getMonth() < 9)? "0" + (date.getMonth() + 1): (date.getMonth() + 1);
        let day = (date.getDate() < 10)? "0" + date.getDate(): date.getDate();
        return day + "-" + month + "-" + date.getFullYear() + " " + hours + ":" + minutes;
    }

    public getAmount(transactionWrapper: any): string {
        return (transactionWrapper.amount / this.coin.convertAmount).toFixed(this.coin.fractionDigits) + " " + this.coin.name;
    }

    public getTransactions(coin: b2bcoinModels.WalletCoin) {
        this.transactions = this.walletService.getPaymentsForCoin(coin);
        this.setPage(1);
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
