import { Routes, RouterModule }  from '@angular/router';

import { ResetPassword } from './resetPassword.component';
import { ModuleWithProviders } from '@angular/core';

// noinspection TypeScriptValidateTypes
export const routes: Routes = [
    {
        path: '',
        component: ResetPassword
    }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
