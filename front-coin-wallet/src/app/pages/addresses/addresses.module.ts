import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgaModule } from '../../theme/nga.module';
import { SimpleNotificationsModule } from 'angular2-notifications';
import { MarkdownModule } from 'angular2-markdown';

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
        SimpleNotificationsModule.forRoot(),
        routing
    ],
    declarations: [
        Addresses,
        Overview
    ]
})
export class AddressesModule {
}
