import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MarkdownModule } from 'angular2-markdown';

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
