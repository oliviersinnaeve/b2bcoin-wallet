import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../../theme/nga.module';
import { MarkdownModule } from 'angular2-markdown';

import { ModalModule } from 'ngx-bootstrap';

import { Settings } from './settings.component';
import { routing } from './settings.routing';


@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        NgaModule,
        ModalModule.forRoot(),
        MarkdownModule.forRoot(),
        routing
    ],
    declarations: [
        Settings
    ]
})
export class SettingsModule {
}
