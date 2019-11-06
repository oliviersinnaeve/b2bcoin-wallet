import { Component, ViewChild, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';

import { WalletService } from '../../../walletService.service';
import { TransactionsService } from '../../../transactions/transactions.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin/model/models';
import { WalletResourceService } from '../../../../services/com.b2beyond.api.webwallet-service-b2bcoin';



@Component({
    selector: 'create',
    styleUrls: ['createPayment.scss'],
    templateUrl: './createPayment.html'
})

export class CreatePayment implements OnInit {

    @ViewChild('createTransferModal', {static: false}) createTransferModal: ModalDirective;
    @ViewChild('messageModal', {static: false}) messageModal: ModalDirective;

    public selectedAddress: b2bcoinModels.AddressBalance = {};
    public fee: number = 0.000001;
    public mixin: number = 0;
    public mixins: Array<number> = [0,1,2,3,4,5,6,7,8,9,10];

    public submitting: boolean = false;

    public addresses: Array<b2bcoinModels.UserAddress> = [];

    public transfers: Array<any> = [];

    public message: string;
    public transactionHash: string;
    public error: boolean;

    public transfer = {
        address: "",
        amount: 0
    };

    public payment: b2bcoinModels.PaymentInput = {
        fee: 0,
        transfers: {}
    };

    constructor (private userState: UserState,
                 private walletApi: WalletResourceService,
                 private walletService: WalletService,
                 private transactionsService: TransactionsService,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        if (this.walletService.selectedCoin.name == "") {
            this.router.navigateByUrl("pages/dashboard/mainWallet");
        }
    }

    public addTransaction() {
        this.transfers.push(this.transfer);
        this.transfer = {
            address: "",
            amount: 0,
        };

        this.createTransferModal.hide();
    }

    public gotoPayment(paymentHash: string) {
        this.transactionsService.searchString = paymentHash;
        this.router.navigateByUrl("pages/transactions/result");
        this.transactionsService.triggerSearch();
    }

    public createPayment() {
        this.submitting = true;
        for (let i = 0; i < this.transfers.length; i++) {
            this.payment.transfers[this.transfers[i].address] = this.transfers[i].amount * this.walletService.selectedCoin.convertAmount;
        }

        this.payment.fee = this.fee * this.walletService.selectedCoin.convertAmount;
        this.payment.anonymity = this.mixin;
        this.payment.addresses = [];
        let address: b2bcoinModels.Address = {};
        address.address = this.selectedAddress.address;
        this.payment.addresses.push(address);
        this.payment.address = this.selectedAddress.address;

        this.walletApi.createPaymentUsingPOST(this.walletService.selectedCoin.name, this.payment).subscribe(result => {
                this.transactionHash = result.transactionHash;

                this.payment = {
                    fee: 0.000001,
                    transfers: {}
                };

                this.error = false;
                this.messageModal.show();
                this.submitting = false;
            },
            (error) => {
                //console.log("Known error", error);
                if (error.status === 401) {
                    this.userState.handleError(error, this.createPayment, this);
                    this.submitting = false;
                }

                if (error.status === 999) {
                    let jsonError = JSON.parse(error._body);
                    this.payment.fee = 0.000001;
                    this.error = true;
                    this.message = jsonError.message;
                    this.messageModal.show();
                    this.submitting = false;
                }
            });
    }

    public deleteTransfer(transfer) {
        //console.log("deleting transfer", transfer);
        if (this.transfers.length == 1) {
            this.transfers = [];
        } else {
            for (let i = 0; i < this.transfers.length; i++) {
                if (this.transfers[i].address == transfer.address && this.transfers[i].amount == transfer.amount) {
                    this.transfers.splice(i, 1);
                }
            }
        }
    }

    public getParsedAmount(amount: number): string {
        if (amount != undefined && amount != null) {
            return ((amount * this.walletService.selectedCoin.convertAmount) / this.walletService.selectedCoin.convertAmount).toFixed(this.walletService.selectedCoin.fractionDigits) + " " + this.walletService.selectedCoin.name;
        } else {
            return (0 / this.walletService.selectedCoin.convertAmount).toFixed(this.walletService.selectedCoin.fractionDigits) + " " + this.walletService.selectedCoin.name;
        }
    }

    public onAmountChange(amount: string) {
        // explicitly update state (one way data flow)
        //console.log("New parsed amount", amount);
    }

    public ngOnInit (): void {
        this.addresses = this.walletService.getAddressesForCoin(this.walletService.selectedCoin);
    }

}
