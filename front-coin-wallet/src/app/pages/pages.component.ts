import { Component } from '@angular/core';
import { Routes } from '@angular/router';

import { TranslateService } from 'ng2-translate';

import { BaMenuService } from '../theme';
import { PAGES_MENU } from './pages.menu';

 //<footer class="al-footer clearfix">
 //  <div class="al-footer-right">Created with <i class="ion-heart"></i></div>
 //  <div class="al-footer-main clearfix">
 //    <div class="al-copy">&copy; <a href="http://akveo.com">Akveo</a> 2016</div>
 //    <ul class="al-share clearfix">
 //      <li><i class="socicon socicon-facebook"></i></li>
 //      <li><i class="socicon socicon-twitter"></i></li>
 //      <li><i class="socicon socicon-google"></i></li>
 //      <li><i class="socicon socicon-github"></i></li>
 //    </ul>
 //  </div>
 //</footer>
@Component({
    selector: 'pages',
    template: `
    <ba-sidebar></ba-sidebar>
    <ba-page-top></ba-page-top>
    <div class="al-main">
      <div class="al-content">
        <!--<ba-content-top></ba-content-top>-->
        <router-outlet></router-outlet>
      </div>
    </div>
    <ba-back-top position="200"></ba-back-top>
    `
})
export class Pages {

    constructor (
        private _menuService: BaMenuService,
        private translate: TranslateService) {

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        var language = navigator.languages && navigator.languages[0].split("-")[0];
        console.log("Using language", language);
        translate.use(language);
    }

    ngOnInit () {
        this._menuService.updateMenuByRoutes(<Routes>PAGES_MENU);
    }
}
