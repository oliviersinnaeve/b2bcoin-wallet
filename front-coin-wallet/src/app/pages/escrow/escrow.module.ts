import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ngx-bootstrap';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { TooltipModule } from 'ngx-bootstrap';

import { routing }       from './escrow.routing';
import { Escrow } from './escrow.component';
import { Overview } from './components/overview/overview.component';
import { CreateEscrowTransaction } from './components/result/createEscrowTransaction.component';

import { Ng2SmartTableModule } from 'ng2-smart-table';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        Ng2SmartTableModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        TooltipModule.forRoot(),
        // shown passing optional global defaults
        AdsenseModule.forRoot({
            adClient: 'ca-pub-5721689054603180',
            adSlot: 7693847990,
        }),
        routing
    ],
    declarations: [
        Escrow,
        Overview,
        CreateEscrowTransaction
    ]
})
export class EscrowModule {
}
