import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { ModalModule } from 'ngx-bootstrap';

import { routing }       from './addresses.routing';
import { Addresses } from './addresses.component';
import { Overview } from './components/overview/overview.component';

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
        Addresses,
        Overview
    ]
})
export class AddressesModule {
}
