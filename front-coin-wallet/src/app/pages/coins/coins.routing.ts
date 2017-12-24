import { Routes, RouterModule }  from '@angular/router';

import { Coins } from './coins.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Coins,
        children: [
            //{path: 'overview', component: Overview}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
