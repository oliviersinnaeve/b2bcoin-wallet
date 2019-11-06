import { Component, ViewChild, OnInit, Input } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { WalletService } from '../../../walletService.service';


import 'rxjs/Rx';
import {FaucetResourceService, WalletResourceService} from "../../../../services/com.b2beyond.api.webwallet-service-b2bcoin";

@Component({
    selector: 'faucet-overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class FaucetOverview implements OnInit {

    @ViewChild('createAddressModal', {static: false}) createAddressModal: ModalDirective;

    public faucets = [];
    public selectedCoin = {};


    constructor (private userState: UserState,
                 private walletApi: WalletResourceService,
                 private faucetApi: FaucetResourceService,
                 private walletService: WalletService,
                 private notificationsService: NotificationsService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.walletService.addressFetchedEmitter
            .subscribe(item => this.initialize(item));

    }

    public ngOnInit(): void {
        if (this.walletService.primaryCoin != undefined) {
            this.initialize(this.walletService.primaryCoin);
        } else {
            this.walletService.addressFetchedEmitter
                .subscribe(item => this.initialize(item));
        }
    }

    private initialize(coin): void {
        console.log("Initialize faucetUserAddress !!!", coin);

        this.faucetApi.getFaucetListUsingGET().subscribe(
            result => {
                console.log("faucet list", result);
                this.faucets = result.filter(value => value.balance.availableBalance > 0);
            },
            error => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.initialize, this);
                }
            }
        );
    }

    public getParsedAmount(amount: number): string {
        if (amount != undefined && amount != null) {
            return (amount / this.walletService.primaryCoin.convertAmount).toFixed(this.walletService.primaryCoin.fractionDigits) + " " + this.walletService.primaryCoin.name;
        } else {
            return (0 / this.walletService.primaryCoin.convertAmount).toFixed(this.walletService.primaryCoin.fractionDigits) + " " + this.walletService.primaryCoin.name;
        }
    }

    public copyToClipboard(value, text) {
        console.log('copyToClipboard', value, text);
        let selBox = document.createElement('textarea');

        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = value;

        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();

        document.execCommand('copy');
        document.body.removeChild(selBox);

        console.log("Trigger notificationService");
        this.notificationsService.success("Copy", text);
    }


}
