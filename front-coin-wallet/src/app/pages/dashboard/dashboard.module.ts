import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ngx-bootstrap';

import { Dashboard } from './dashboard.component';
import { routing } from './dashboard.routing';


import { WalletInfo } from './walletInfo';
import { ServerInfo } from './serverInfo';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 6949772221,
        }),
        routing
    ],
    declarations: [
        WalletInfo,
        ServerInfo,
        Dashboard
    ]
})
export class DashboardModule {
}
