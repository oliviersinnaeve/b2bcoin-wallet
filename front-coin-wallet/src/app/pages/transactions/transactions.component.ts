import {Component} from '@angular/core';

import 'style-loader!./transactions.scss';

@Component({
    selector: 'transactions',
    template: `<router-outlet></router-outlet><simple-notifications [options]="options"></simple-notifications>`
})
export class Transactions {

    public options = {
        position: ["top", "right"],
        timeOut: 5000,
        lastOnBottom: false
    };

    constructor () {
    }

}
