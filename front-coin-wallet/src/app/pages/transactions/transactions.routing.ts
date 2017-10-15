import { Routes, RouterModule }  from '@angular/router';

import { Transactions } from './transactions.component';
import { Overview } from './components/overview/overview.component';
import { SingleResult } from './components/result/singleResult.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Transactions,
        children: [
            {path: 'overview', component: Overview},
            {path: 'result', component: SingleResult}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
