import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../../theme/nga.module';

import { TranslateModule } from '@ngx-translate/core';

import { Login } from './login.component';
import { routing }       from './login.routing';


@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        NgaModule,
        TranslateModule.forRoot(),
        routing
    ],
    declarations: [
        Login
    ]
})
export class LoginModule {
}
