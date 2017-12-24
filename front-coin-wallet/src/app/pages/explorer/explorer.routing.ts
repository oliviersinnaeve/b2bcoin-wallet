import { Routes, RouterModule }  from '@angular/router';

import { Explorer } from './explorer.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Explorer
    }
];

export const routing = RouterModule.forChild(routes);
