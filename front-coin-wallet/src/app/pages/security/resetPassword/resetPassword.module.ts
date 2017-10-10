import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../../theme/nga.module';

import { ResetPassword } from './resetPassword.component';
import { routing }       from './resetPassword.routing';


@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        NgaModule,
        routing
    ],
    declarations: [
        ResetPassword
    ]
})
export class ResetPasswordModule {
}
