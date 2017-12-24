import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ngx-bootstrap';

import { SimpleNotificationsModule } from 'angular2-notifications';

import { Dashboard } from './dashboard.component';
import { routing } from './dashboard.routing';

import { TooltipModule } from 'ngx-bootstrap';
import { TranslateModule } from 'ng2-translate';


import { WalletInfo } from './walletInfo';
import { WalletInfoFull } from './walletInfoFull';
import { MultiWallet } from './multiWallet';
import { MultiWalletInfoFull } from './multiWalletInfoFull';
import { ServerInfo } from './serverInfo';
import { Overview } from '../addresses/components/overview/overview.component';
import { PaymentOverview } from '../payments/components/overview/overview.component';
import { TransactionOverview } from '../transactions/components/overview/overview.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        TooltipModule.forRoot(),
        TranslateModule.forRoot(),
        SimpleNotificationsModule.forRoot(),
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 6949772221,
        }),
        routing
    ],
    declarations: [
        MultiWalletInfoFull,
        MultiWallet,
        WalletInfoFull,
        WalletInfo,
        ServerInfo,
        Dashboard,
        Overview,
        PaymentOverview,
        TransactionOverview
    ]
})
export class DashboardModule {
}
