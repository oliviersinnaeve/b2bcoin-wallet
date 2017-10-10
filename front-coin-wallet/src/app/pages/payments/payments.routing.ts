import { Routes, RouterModule }  from '@angular/router';

import { Payments } from './payments.component';
import { Overview } from './components/overview/overview.component';
import { CreatePayment } from './components/create/createPayment.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Payments,
        children: [
            {path: 'overview', component: Overview},
            {path: 'create-payment', component: CreatePayment}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
