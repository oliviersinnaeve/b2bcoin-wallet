import { Routes, RouterModule }  from '@angular/router';

import { Payments } from './payments.component';
//import { PaymentOverview } from './components/overview/overview.component';
import { CreatePayment } from './components/create/createPayment.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Payments,
        children: [
            //{path: 'payment-overview', component: PaymentOverview},
            {path: 'create', component: CreatePayment}
        ]
    }
];

export const routing = RouterModule.forChild(routes);
