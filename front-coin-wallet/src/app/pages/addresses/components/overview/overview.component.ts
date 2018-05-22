import { Component, ViewChild, Input } from '@angular/core';
import { Router } from "@angular/router";
import { UserState } from '../../../../user.state';
import { ModalDirective } from 'ngx-bootstrap';

import { TranslateService } from 'ng2-translate';
import { WalletService } from '../../../walletService.service';

import { NotificationsService } from 'angular2-notifications';
import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import 'rxjs/Rx';

@Component({
    selector: 'address-overview',
    styleUrls: ['./overview.scss'],
    templateUrl: './overview.html'
})

export class Overview {

    @Input('coin')
    public coin: b2bcoinModels.WalletCoin;

    @Input('showCoinLogo')
    public showCoinLogo: boolean = true;

    @ViewChild('confirmDeleteAddressModal') confirmDeleteAddressModal: ModalDirective;
    @ViewChild('deleteAddressModal') deleteAddressModal: ModalDirective;
    @ViewChild('viewKeysModal') viewKeysModal: ModalDirective;

    public keys: b2bcoinModels.WalletKeys = {};

    //public addresses: Array<b2bcoinModels.AddressBalance> = [];
    private addressToDelete : b2bcoinModels.AddressBalance;


    constructor (private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private notificationsService: NotificationsService,
                 private translate: TranslateService,
                 private router: Router) {
        this.walletApi.defaultHeaders = userState.getExtraHeaders();

        //this.walletService.getAddresses(false);
    }

    public deleteAddress () {
        this.confirmDeleteAddressModal.hide();

        this.walletApi.deleteAddress(this.coin.name, { address: this.addressToDelete.address }).subscribe(
            result => {

                this.addressToDelete = undefined;
                this.walletService.addresses = [];
                this.walletService.addressBalances = {};
                this.walletService.getAddresses(false);

                this.notificationsService.success(this.translate.instant('DELETE_ADDRESS_TITLE'), this.translate.instant('DELETE_ADDRESS_SUCCESS'), {
                        timeOut: 3000,
                        showProgressBar: true,
                        pauseOnHover: true,
                        clickToClose: true
                    }
                );
            },
            error => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.deleteAddress, this);
                }
            }
        );
    }

    public setDeleteAddress(item) {
        this.addressToDelete = item;
        this.confirmDeleteAddressModal.show();
    }

    public showViewKeys(item) {
        this.keys = {
            publicViewKey: "Loading ...",
            privateViewKey: "Loading ...",
            privateSpendKey: "Loading ..."
        };
        this.walletService.getSpendKeysObservable(this.coin, item.address).subscribe(
            (success) => {
                this.keys = success;
            },
            (error) => {
                if (error.status === 401) {
                    this.userState.handleError(error, this.showViewKeys, this);
                }
            }
        );

        this.viewKeysModal.show();
    }

    public copyAddressToClipboard(item) {
        let selBox = document.createElement('textarea');

        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = item.address;

        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();

        document.execCommand('copy');
        document.body.removeChild(selBox);

        this.notificationsService.success("Copy", "Address copied to clipboard");
    }

}
