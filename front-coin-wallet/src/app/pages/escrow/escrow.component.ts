import {Component} from '@angular/core';

import 'style-loader!./escrow.scss';

@Component({
    selector: 'escrow',
    template: `<router-outlet></router-outlet><simple-notifications [options]="options"></simple-notifications>`
})
export class Escrow {

    public options = {
        position: ["top", "right"],
        timeOut: 5000,
        lastOnBottom: false
    };

    constructor () {
    }

}
