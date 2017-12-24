import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../../theme/nga.module';

import { TranslateModule } from 'ng2-translate';

import { ResetPassword } from './resetPassword.component';
import { routing }       from './resetPassword.routing';


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
        ResetPassword
    ]
})
export class ResetPasswordModule {
}
