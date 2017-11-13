import { Routes, RouterModule }  from '@angular/router';

import { Escrow } from './escrow.component';
import { Overview } from './components/overview/overview.component';
import { CreateEscrowTransaction } from './components/result/createEscrowTransaction.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Escrow,
        children: [
            {path: 'overview', component: Overview},
            {path: 'create', component: CreateEscrowTransaction}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
