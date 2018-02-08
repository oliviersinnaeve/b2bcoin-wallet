import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { SimpleNotificationsModule } from 'angular2-notifications';
import { TranslateModule } from 'ng2-translate';
import { Ng2SmartTableModule } from 'ng2-smart-table';

import { ModalModule } from 'ngx-bootstrap';
import { TooltipModule } from 'ngx-bootstrap';

import { routing }       from './faucet.routing';
import { Faucet }       from './faucet.component';
import { FaucetOverview } from './components/overview/overview.component';
import { FaucetOwner } from './components/owner/overview.component';
import { FaucetUserOverview } from './components/user/overview.component';

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
        Ng2SmartTableModule,
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 6949772221,
        }),
        routing
    ],
    declarations: [
        Faucet,
        FaucetOverview,
        FaucetOwner,
        FaucetUserOverview
    ]
})
export class FaucetModule {
}
