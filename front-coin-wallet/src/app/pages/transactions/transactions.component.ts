import {Component} from '@angular/core';


@Component({
    selector: 'transactions',
    template: `<router-outlet></router-outlet><simple-notifications [options]="options"></simple-notifications>`,
    styleUrls:['./transactions.scss']
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
