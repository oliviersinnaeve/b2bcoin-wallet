import { Component, ViewChild, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDirective } from 'ngx-bootstrap';

import { WalletService } from '../../walletService.service';


@Component({
    selector: 'multi-wallet',
    templateUrl: './multiWallet.html'
})
export class MultiWallet {

    /**
     * Do not remove the walletService, it is used in the multiWallet.html
     *
     * @param walletService used in html
     */
    constructor (private walletService: WalletService) { }

}
