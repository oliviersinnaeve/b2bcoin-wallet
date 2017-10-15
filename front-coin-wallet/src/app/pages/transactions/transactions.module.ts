import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { ModalModule } from 'ngx-bootstrap';
import { SimpleNotificationsModule } from 'angular2-notifications';
import { MarkdownModule } from 'angular2-markdown';

import { routing }       from './transactions.routing';
import { Transactions } from './transactions.component';
import { Overview } from './components/overview/overview.component';
import { SingleResult } from './components/result/singleResult.component';

import { Ng2SmartTableModule } from 'ng2-smart-table';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgaModule,
        Ng2SmartTableModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        SimpleNotificationsModule.forRoot(),
        routing
    ],
    declarations: [
        Transactions,
        Overview,
        SingleResult
    ]
})
export class TransactionsModule {
}
