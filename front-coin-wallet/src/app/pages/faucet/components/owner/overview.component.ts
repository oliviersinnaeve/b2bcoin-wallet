import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserState} from '../../../../user.state';
import {ModalDirective} from 'ngx-bootstrap';
import {NotificationsService} from 'angular2-notifications';

import {LocalDataSource} from 'ng2-smart-table';

import {WalletServiceStore} from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import {FaucetService, WalletService} from '../../../../services/com.b2beyond.api.b2bcoin';

import {baseUrl, websiteId} from '../../../../environment-config';


import 'rxjs/Rx';

@Component({
    selector: 'faucet-owner',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class FaucetOwner implements OnInit {

    @ViewChild('createAddressModal') createAddressModal: ModalDirective;

    public creatingWallet = false;

    public source: LocalDataSource = new LocalDataSource();

    public nameInEdit: boolean = false;
    public faucetUrlInEdit: boolean = false;

    public selectedCoin = {};

    public settings = {
        actions: false,
        columns: {
            address: {
                title: 'Address'
            },
            balance: {
                title: 'Balance',
                valuePrepareFunction: (value) => {
                    return this.getParsedAmount(value);
                }
            },
            paid: {
                title: 'Paid',
                valuePrepareFunction: (value) => {
                    return this.getParsedAmount(value);
                }
            }
        }
    };

    constructor (private userState: UserState,
                 private WalletService: WalletService,
                 private FaucetService: FaucetService,
                 private walletService: WalletServiceStore,
                 private notificationsService: NotificationsService,
                 private router: Router) {
        this.walletService.faucetAddressUpdateEmitter
            .subscribe(item => this.faucetAddressUpdated(item));

        this.walletService.faucetAddressPaymentsFetchedEmitter
            .subscribe(items => this.setData(items));
    }

    public ngOnInit(): void {
        this.WalletService.defaultHeaders = this.userState.getExtraHeaders(this.WalletService.defaultHeaders);

        if (this.walletService.primaryCoin != undefined) {
            this.faucetAddressUpdated(this.walletService.faucetAddress);
            this.setData(this.walletService.faucetAddressPayments);
        } else {
            this.walletService.faucetAddressUpdateEmitter
                .subscribe(item => this.faucetAddressUpdated(item));

            this.walletService.faucetAddressPaymentsFetchedEmitter
                .subscribe(items => this.setData(items));
        }
    }

    private faucetAddressUpdated(item): void {
        if (item != undefined) {
            this.nameInEdit = false;
            this.faucetUrlInEdit = false;
        }
    }

    public createNewFaucetAddress(faucetUser: boolean) {
        if (!this.creatingWallet) {
            this.creatingWallet = true;

            let userAddress: b2bcoinModels.UserAddress = {};
            userAddress.currency = this.walletService.primaryCoin;

            let request: b2bcoinModels.CreateFaucetAddressRequest = {
                websiteId: websiteId,
                endpointRoot: baseUrl + "/b2bcoin/api/faucet",
                userAddress: userAddress,
                faucetUser: faucetUser
            };

            this.FaucetService.createFaucetAddress(request).subscribe(
                    result => {
                        this.creatingWallet = false;
                        //if (faucetUser) {
                        //    this.faucetUserAddress = result;
                        //    this.hasFaucetUserAddress = true;
                        //} else {
                            this.walletService.faucetAddress = result;
                            this.walletService.hasFaucetAddress = true;
                        //}
                },
                    error => {
                    if (error.status === 401) {
                        this.creatingWallet = false;
                        this.userState.handleError(error, this.createNewFaucetAddress, this);
                    }
                }
            );
        }
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

    private setData(json) {
        console.log("Analysing data", json);
        if (json.constructor === Array) {
            this.setDataArray(json);
        }
    }

    private setDataArray(data) {
        let tmpSettings = JSON.parse(JSON.stringify(this.settings));
        //
        //if (data.length > 0) {
        //    tmpSettings.columns = {};
        //
        //    let row = data[0];
        //    for (var key in row) {
        //        if (row.hasOwnProperty(key)) {
        //            tmpSettings.columns[key] = {};
        //            tmpSettings.columns[key].title = key;
        //        }
        //    }
        //}

        //this.settings = tmpSettings;
        this.source.load(data);
    }


}
