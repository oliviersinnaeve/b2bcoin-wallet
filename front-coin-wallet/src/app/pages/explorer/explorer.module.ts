import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { MarkdownModule } from 'angular2-markdown';
import { AdsenseModule } from 'ng2-adsense';

import { TranslateModule } from 'ng2-translate';

import { ModalModule } from 'ngx-bootstrap';
import { TooltipModule } from 'ngx-bootstrap';

import { routing }       from './explorer.routing';
import { Explorer }       from './explorer.component';
import { ExplorerResult } from './components/overview/overview.component';

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
        Explorer,
        ExplorerResult
    ]
})
export class ExplorerModule {
}
