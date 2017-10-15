import { Component, ViewChild } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';

import { WalletService } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';


import { B2BCurrencyFormatterDirective } from "../../../../theme/directives/formatter/b2bCurrencyFormatter.directive";

@Component({
    selector: 'create',
    templateUrl: './createPayment.html'
})

export class CreatePayment {

    @ViewChild('createTransferModal') createTransferModal: ModalDirective;
    @ViewChild('messageModal') messageModal: ModalDirective;

    public selectedAddress: b2bcoinModels.AddressBalance = {};

    public submitting: boolean = false;

    public addresses: Array<b2bcoinModels.AddressBalance> = [];

    public transfers: Array<any> = [];

    public transactionHash: string;

    public message: srting;
    public error: boolean;

    public transfer = {
        address: "",
        amount: 0
    };

    public payment: b2bcoinModels.PaymentInput = {
        fee: 0.000001,
        transfers: {}
    };

    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        this.initialize();
    }

    public addTransaction() {
        this.transfers.push(this.transfer);
        this.transfer = {
            address: "",
            amount: 0,
        };

        this.createTransferModal.hide();
    }

    public createPayment() {
        this.submitting = true;
        for (let i = 0; i < this.transfers.length; i++) {
            this.payment.transfers[this.transfers[i].address] = this.transfers[i].amount * 1000000000000;
        }

        this.payment.fee = this.payment.fee * 1000000000000;
        this.payment.addresses = [];
        let address = {};
        address.address = this.selectedAddress.address;
        this.payment.addresses.push(address);
        this.payment.address = this.selectedAddress.address;

        this.walletApi.createPayment(this.payment).subscribe(result => {
                this.paymentHash = result.transactionHash;

                this.payment = {
                    fee: 0.000001,
                    transfers: {}
                };

                this.error = false;
                this.message = "Payment executed: " + "<a (click)=''>" + this.paymentHash + "</a>";
                this.messageModal.show();
                this.submitting = false;
            },
            (error) => {
                console.log("Known error", error);
                if (error.status === 401) {
                    this.userState.handleError(error, this.initialize, this);
                    this.submitting = false;
                }

                if (error.status === 999) {
                    let jsonError = JSON.parse(error._body);
                    this.payment.fee = 0.000001;
                    this.error = true;
                    this.message = "Payment failed: " + jsonError.message;
                    this.messageModal.show();
                    this.submitting = false;
                }
            });
    }

    public getParsedAmount(amount: number): string {
        if (amount != undefined && amount != null) {
            return ((amount * 1000000000000) / 1000000000000).toFixed(12) + " B2B";
        } else {
            return (0 / 1000000000000).toFixed(12) + " B2B";
        }
    }

    public onAmountChange(amount: string) {
        // explicitly update state (one way data flow)
        console.log("New parsed amount", amount);
    }

    private initialize () {
        this.walletService.getAddressesObservable().subscribe(result => {
            this.addresses = result;
        },
        (error) => {
            if (error.status === 401) {
                this.userState.handleError(error, this.initialize, this);
            }
        });
    }

}
