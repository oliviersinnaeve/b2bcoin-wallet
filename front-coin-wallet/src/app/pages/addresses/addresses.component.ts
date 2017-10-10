import {Component} from '@angular/core';

@Component({
    selector: 'addresses',
    template: `<router-outlet></router-outlet><simple-notifications [options]="options"></simple-notifications>`
})
export class Addresses {

    public options = {
        position: ["top", "right"],
        timeOut: 5000,
        lastOnBottom: false
    };

    constructor () {
    }

}
