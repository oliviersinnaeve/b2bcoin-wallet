import { NgModule, ErrorHandler } from '@angular/core';
import { CommonModule } from '@angular/common';

import { routing } from './pages.routing';
import { NgaModule } from '../theme/nga.module';

import { Pages } from './pages.component';

import { UserState } from '../user.state';

import { FacebookModule, FacebookService } from 'ngx-facebook';


@NgModule({
    imports: [
        CommonModule,
        NgaModule,
        FacebookModule.forRoot(),
        routing],
    declarations: [
        Pages
    ],
    providers: [
        FacebookService
    ]
})
export class PagesModule {
}
