import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';


import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { EscrowApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/EscrowApi';

import { EscrowService } from '../../escrow.service';
import { WalletService } from '../../../walletService.service';


@Component({
    selector: 'create-escrow-transaction',
    templateUrl: './createEscrowTransaction.html',
})

export class CreateEscrowTransaction implements OnInit {

    @ViewChild('receiveSellModal') receiveSellModal: ModalDirective;

    public escrowTransaction: b2bcoinModels.EscrowTransaction = {};

    public loading: boolean = false;

    public coins: Array<string> = [];

    constructor (private userState: UserState,
                 private escrowApi: EscrowApi,
                 private escrowService: EscrowService,
                 private walletService: WalletService,
                 private router: Router) {

        this.escrowApi.defaultHeaders = userState.getExtraHeaders();
        this.escrowTransaction.sellEmail = userState.getUser().userId;
        this.escrowTransaction.sellAmount = 0;
        this.escrowTransaction.buyAmount = 0;
    }

    public ngOnInit() {
        this.escrowApi.getProvidedCoins().subscribe(
                result => {
                    this.coins = result;
                },
                error => {
                    if (error.status === 401) {
                        this.userState.handleError(error, this.ngOnInit, this);
                    }
                }
        );
    }

    public createEscrowTransaction() {
        this.loading = true;
        this.escrowApi.createService(this.escrowTransaction).subscribe(
            result => {
                this.escrowTransaction = result;
                this.receiveSellModal.show();

                var deadline = new Date(new Date().getMilliseconds() + 30 * 60 * 1000);
                this.initializeClock('clockdiv', deadline);

                this.loading = false;
                this.pollRequest(result);
            },
            error => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.createEscrowTransaction, this);
                }
            }
        );
    }

    public pollRequest(result) {
        var context = this;
        let interval = setInterval(function(result) {
            context.escrowApi.pollResult(result).subscribe(
                    result => {
                    if (result.sellReceived) {
                        context.escrowTransaction = result;
                        clearInterval(interval);
                    }
                },
                    error => {
                    clearInterval(interval);
                    if (error.status === 401) {
                        this.userState.handleError(error, this.pollRequest, this);
                    }
                }
            );
        }, 60000, result);
    }

    public newTransaction() {
        this.escrowTransaction = {};
        this.receiveSellModal.hide();
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
                clearInterval(timeinterval);
            }
        }

        updateClock();
        var timeinterval = setInterval(updateClock, 1000);
    }

}
