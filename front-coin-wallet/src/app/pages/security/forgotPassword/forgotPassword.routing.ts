import { Routes, RouterModule }  from '@angular/router';

import { ForgotPassword } from './forgotPassword.component';
import { ModuleWithProviders } from '@angular/core';

// noinspection TypeScriptValidateTypes
export const routes: Routes = [
    {
        path: '',
        component: ForgotPassword
    }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
