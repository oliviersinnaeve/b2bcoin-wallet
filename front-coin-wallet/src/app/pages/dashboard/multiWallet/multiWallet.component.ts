import { Component, ViewChild, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletService } from '../../walletService.service';


@Component({
    selector: 'multi-wallet',
    templateUrl: './multiWallet.html'
})
export class MultiWallet {

    constructor (private walletService: WalletService) {
        //this.walletService.getAddresses(this.ngOnInit, this);
    }

}
