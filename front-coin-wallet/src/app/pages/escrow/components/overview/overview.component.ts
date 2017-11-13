import { Component, ViewChild, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Http, Headers, RequestOptionsArgs, Response, URLSearchParams } from '@angular/http';
import { ModalDirective } from 'ngx-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { UserState } from '../../../../user.state';
import { WalletService } from '../../../walletService.service';
import { EscrowService } from '../../escrow.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { EscrowApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/EscrowApi';

import { websiteId } from '../../../../environment';

@Component({
    selector: 'overview',
    templateUrl: './overview.html',
})

export class Overview implements OnInit {

    @ViewChild('acceptTransactionModal') acceptTransactionModal: ModalDirective;
    @ViewChild('waitForPaymentModal') waitForPaymentModal: ModalDirective;


    public loading: boolean = true;
    public timeout: boolean = false;
    public escrows: Array<b2bcoinModels.EscrowTransaction> = [];

    public escrowTransaction: b2bcoinModels.EscrowTransaction = {};

    constructor (private notificationsService: NotificationsService,
                 private userState: UserState,
                 private escrowApi: EscrowApi,
                 private walletService: WalletService,
                 private escrowService: EscrowService,
                 private http: Http,
                 private router: Router) {
        this.escrowApi.defaultHeaders = userState.getExtraHeaders();
    }

    public ngOnInit (): void {
        this.escrowApi.checkAllEscrows().subscribe(
            result => {
                this.escrowApi.getEscrows().subscribe(
                        result => {
                        this.escrows = result;
                        this.loading = false;
                    },
                        error => {
                        this.notificationsService.alert("Error", "Something went wrong");
                    }
                );
            },
            error => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.ngOnInit, this);
                }
            }
        );


        var context = this;
        let interval = setInterval(function() {
            context.escrowApi.checkAllEscrows().subscribe(
                    result => {
                        context.escrowApi.getEscrows().subscribe(
                            result => {
                                context.escrows = result;
                                context.loading = false;
                        },
                            error => {
                                context.notificationsService.alert("Error", "Something went wrong");
                        }
                    );
                },
                    error => {
                    if (error.status === 401) {
                        context.userState.handleError(error, context.ngOnInit, this);
                    }
                }
            );
        }, 60000);
    }

    public acceptTrade(escrowTransaction: b2bcoinModels.EscrowTransaction) {
        this.escrowTransaction = escrowTransaction;
        this.acceptTransactionModal.show();
    }

    public updateTrade() {
        this.loading = true;
        this.escrowApi.acceptTrade(this.escrowTransaction).subscribe(
            result => {
                this.acceptTransactionModal.hide();
                this.showPaymentDialog(result);
            },
            error => {
                this.loading = false;
                if (error.status === 401) {
                    this.userState.handleError(error, this.updateTrade, this);
                }
            }
        );
    }

    public showPaymentDialog(escrowTransaction: b2bcoinModels.EscrowTransaction) {
        this.timeout = false;
        if (escrowTransaction) {
            this.escrowTransaction = escrowTransaction;
        }

        this.waitForPaymentModal.show();
        this.loading = false;

        var deadline = new Date(new Date().getMilliseconds() + (30 * 60 * 1000));
        this.initializeClock('clockdiv', deadline);

        this.pollResult();
    }

    public pollResult() {
        var context = this;
        let interval = setInterval(function(result) {
            context.escrowApi.pollResult(result).subscribe(
                result => {
                    if (result.buyReceived) {
                        context.escrowTransaction = result;
                        clearInterval(interval);
                    }

                    if (context.timeout) {
                        clearInterval(interval);
                    }
                },
                    error => {
                    if (error.status === 401) {
                        context.userState.handleError(error, this.pollResult, this);
                    } else {
                        clearInterval(interval);
                    }
                }
            );
        }, 5000, this.escrowTransaction);
    }

    public getTimeRemaining(endtime) {
        var t = Date.parse(endtime) - new Date().getMilliseconds();
        var seconds = Math.floor((t / 1000) % 60);
        var minutes = Math.floor((t / 1000 / 60) % 60);
        //var hours = Math.floor((t / (1000 * 60 * 60)) % 24);
        //var days = Math.floor(t / (1000 * 60 * 60 * 24));
        return {
            'total': t,
            //'days': days,
            //'hours': hours,
            'minutes': minutes,
            'seconds': seconds
        };
    }

    public initializeClock(id, endtime) {
        var clock = document.getElementById(id);
        //var daysSpan = clock.querySelector('.days');
        //var hoursSpan = clock.querySelector('.hours');
        var minutesSpan = clock.querySelector('.minutes');
        var secondsSpan = clock.querySelector('.seconds');

        var context = this;

        function updateClock() {
            var t = context.getTimeRemaining(endtime);

            //daysSpan.innerHTML = t.days;
            //hoursSpan.innerHTML = ('0' + t.hours).slice(-2);
            minutesSpan.innerHTML = ('0' + t.minutes).slice(-2);
            secondsSpan.innerHTML = ('0' + t.seconds).slice(-2);

            if (t.total <= 0) {
                clearInterval(timeInterval);
                context.timeout = true;
            }
        }

        updateClock();
        var timeInterval = setInterval(updateClock, 1000);
    }

}
