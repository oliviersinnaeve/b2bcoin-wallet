import { Routes, RouterModule }  from '@angular/router';

import { Transactions } from './transactions.component';
import { TransactionOverview } from './components/overview';
// import { SingleResult } from './components/result/singleResult.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {path: '', component: Transactions},
    {path: 'result', component: TransactionOverview}
];

export const routing = RouterModule.forChild(routes);
