import { Component, ViewContainerRef } from '@angular/core';


import { GlobalState } from './global.state';
import { BaImageLoaderService, BaThemePreloader, BaThemeSpinner } from './theme/services';
import { BaThemeConfig } from './theme';
import { layoutPaths } from './theme';

import { TranslateService } from '@ngx-translate/core';


/*
 * App Component
 * Top Level Component
 */
@Component({
    selector: 'app',
    styleUrls: ['./app.scss', './theme/initial.scss'],
    template: `
    <main [ngClass]="{'menu-collapsed': isMenuCollapsed}" baThemeRun>
      <div class="additional-bg"></div>
      <router-outlet></router-outlet>      
    </main>
  `
})
export class App {

    isMenuCollapsed: boolean = false;

    constructor (private _state: GlobalState,
                 private _imageLoader: BaImageLoaderService,
                 private _spinner: BaThemeSpinner,
                 private viewContainerRef: ViewContainerRef,
                 private themeConfig: BaThemeConfig,
                 private translate: TranslateService) {

        themeConfig.config();

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');

        // the lang to use, if the lang isn't available, it will use the current loader to get them
        let language = navigator.languages && navigator.languages[0].split("-")[0];
        translate.use(language);

        this._loadImages();

        this._state.subscribe('menu.isCollapsed', (isCollapsed) => {
            this.isMenuCollapsed = isCollapsed;
        });
    }

    public ngAfterViewInit (): void {
        // hide spinner once all loaders are completed
        BaThemePreloader.load().then((values) => {
            this._spinner.hide();
        });
    }

    private _loadImages (): void {
        // register some loaders
        BaThemePreloader.registerLoader(this._imageLoader.load(layoutPaths.images.root + 'sky-bg.jpg'));
    }

}
