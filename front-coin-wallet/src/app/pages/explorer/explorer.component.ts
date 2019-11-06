import {Component} from '@angular/core';

import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'explorer',
    styleUrls: ['./explorer.scss'],
    templateUrl: './explorer.html'
})
export class Explorer {

    constructor (private translate: TranslateService) {
        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        translate.use(language);
    }

}
