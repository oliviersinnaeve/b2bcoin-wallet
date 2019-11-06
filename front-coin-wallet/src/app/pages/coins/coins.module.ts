import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';
import { TranslateModule } from '@ngx-translate/core';

import { ModalModule } from 'ngx-bootstrap';
import { TooltipModule } from 'ngx-bootstrap';

import { routing }       from './coins.routing';
import { Coins }       from './coins.component';
import { CoinOverview } from './components/overview';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        TooltipModule.forRoot(),
        TranslateModule.forRoot(),
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 6949772221,
        }),
        routing
    ],
    declarations: [
        Coins,
        CoinOverview
    ]
})
export class CoinsModule {
}
