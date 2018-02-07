import { Routes, RouterModule }  from '@angular/router';

import { Faucet } from './faucet.component';
import { FaucetOverview } from './components/overview/overview.component';
import { FaucetOwner } from './components/owner/overview.component';
import { FaucetUserOverview } from './components/user/overview.component';


const routes: Routes = [
    {
        path: '',
        component: Faucet,
        children: [
            { path: 'overview', component: FaucetOverview },
            { path: 'owner', component: FaucetOwner },
            { path: 'user', component: FaucetUserOverview }
        ]
    }
];

//const routes: Routes = [
//    {
//        path: '',
//        component: Faucet
//    }
//];

export const routing = RouterModule.forChild(routes);
