import {Component} from '@angular/core';

import 'style-loader!./payments.scss';

@Component({
    selector: 'payments',
    template: `<router-outlet></router-outlet><simple-notifications [options]="options"></simple-notifications>`
})
export class Payments {

    public options = {
        position: ["top", "right"],
        timeOut: 5000,
        lastOnBottom: false
    };

    constructor () {
    }

}
