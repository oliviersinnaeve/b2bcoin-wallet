import {Component} from '@angular/core';

import { TranslateService } from 'ng2-translate';


@Component({
    selector: 'coins',
    styleUrls: ['./coins.scss'],
    templateUrl: './coins.html'
})
export class Coins {

    constructor (private translate: TranslateService) {
        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        translate.use(language);
    }

}
