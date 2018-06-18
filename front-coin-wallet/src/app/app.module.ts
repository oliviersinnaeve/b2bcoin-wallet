import {ApplicationRef, NgModule} from '@angular/core';
import {CookieService} from 'angular2-cookie/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {createInputTransfer, createNewHosts, removeNgStyles} from '@angularclass/hmr';
import {MarkdownModule} from 'angular2-markdown';
import {TranslateModule} from 'ng2-translate';

import {AdsenseModule} from 'ng2-adsense';

import {TooltipModule} from 'ngx-bootstrap';


import * as user from './services/com.b2beyond.api.user';
import {ApiModule} from './services/com.b2beyond.api.user';
import * as b2bcoin from './services/com.b2beyond.api.b2bcoin';
import {ApiModuleCoin} from './services/com.b2beyond.api.b2bcoin';
/*
 * Platform and Environment providers/directives/pipes
 */
import {baseUrl, ENV_PROVIDERS} from './environment-config';
import {routing} from './app.routing';
// App is our top level component
import {App} from './app.component';
import {UserState} from './user.state';
import {WalletServiceStore} from './pages';
import {TransactionsService} from './pages/transactions/transactions.service';
import {AppState, InternalStateType} from './app.service';
import {GlobalState} from './global.state';
import {NgaModule} from './theme/nga.module';
import {SimpleNotificationsModule} from 'angular2-notifications';
import {PagesModule} from './pages/pages.module';

import {PagerService} from './services/pager.service';
import {HttpClientModule} from "@angular/common/http";


// Application wide providers
const APP_PROVIDERS = [
    CookieService,
    AppState,
    UserState,
    WalletServiceStore,
    TransactionsService,
    GlobalState,
    PagerService
];

export type StoreType = {
    state: InternalStateType,
    restoreInputValues: () => void,
    disposeOldHosts: () => void
};

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [App],
    declarations: [
        App
    ],
    imports: [ // import Angular's modules
        BrowserModule,
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 6949772221,
        }),
        HttpClientModule,
        RouterModule,
        FormsModule,
        ReactiveFormsModule,
        NgaModule.forRoot(),
        TooltipModule.forRoot(),
        SimpleNotificationsModule.forRoot(),
        MarkdownModule.forRoot(),
        PagesModule,
        ApiModule,
        ApiModuleCoin,
        TranslateModule.forRoot(),
        routing
    ],
    providers: [ // expose our Services and Providers into Angular's dependency injection
        ENV_PROVIDERS,
        APP_PROVIDERS,
        {provide: user.BASE_PATH, useValue: baseUrl + "/user/api"},
        {provide: b2bcoin.BASE_PATH, useValue: baseUrl + "/b2bcoin/api"}
    ]
})

export class AppModule {

    constructor (public appRef: ApplicationRef, public appState: AppState, public userState: UserState) {
        userState.navigateAccordingly();
    }

    hmrOnInit (store: StoreType) {
        if (!store || !store.state) return;
        console.log('HMR store', JSON.stringify(store, null, 2));
        // set state
        this.appState._state = store.state;
        // set input values
        if ('restoreInputValues' in store) {
            let restoreInputValues = store.restoreInputValues;
            setTimeout(restoreInputValues);
        }
        this.appRef.tick();
        delete store.state;
        delete store.restoreInputValues;
    }

    hmrOnDestroy (store: StoreType) {
        const cmpLocation = this.appRef.components.map(cmp => cmp.location.nativeElement);
        // save state
        const state = this.appState._state;
        store.state = state;
        // recreate root elements
        store.disposeOldHosts = createNewHosts(cmpLocation);
        // save input values
        store.restoreInputValues = createInputTransfer();
        // remove styles
        removeNgStyles();
    }

    hmrAfterDestroy (store: StoreType) {
        // display new elements
        store.disposeOldHosts();
        delete store.disposeOldHosts;
    }
}
