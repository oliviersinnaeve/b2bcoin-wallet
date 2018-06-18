import {Component} from '@angular/core';

import {WalletServiceStore} from '../../walletService.service';


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
    constructor (private walletService: WalletServiceStore) { }

}
