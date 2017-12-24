import {Component} from '@angular/core';

import { TranslateService } from 'ng2-translate';

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

    constructor (private translate: TranslateService) {
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        translate.use(language);
    }

}
