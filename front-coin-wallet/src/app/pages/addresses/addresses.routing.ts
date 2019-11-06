import { Routes, RouterModule }  from '@angular/router';

import { Addresses } from './addresses.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Addresses,
        children: [
            //{path: 'overview', component: Overview}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
