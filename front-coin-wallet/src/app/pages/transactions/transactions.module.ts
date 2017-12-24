import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ngx-bootstrap';
import { MarkdownModule } from 'angular2-markdown';

import { TranslateModule } from 'ng2-translate';

import { routing }       from './transactions.routing';
import { Transactions } from './transactions.component';

import { Ng2SmartTableModule } from 'ng2-smart-table';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        Ng2SmartTableModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        TranslateModule.forRoot(),
        routing
    ],
    declarations: [
        Transactions
    ]
})
export class TransactionsModule {
}
