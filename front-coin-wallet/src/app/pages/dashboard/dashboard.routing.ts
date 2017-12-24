import { Routes, RouterModule }  from '@angular/router';

import { Dashboard } from './dashboard.component';
import { ModuleWithProviders } from '@angular/core';

import { MultiWallet } from './multiWallet/multiWallet.component';
import { MultiWalletInfoFull } from './multiWalletInfoFull/multiWalletInfoFull.component';
import { WalletInfoFull } from './walletInfoFull/walletInfoFull.component';

// noinspection TypeScriptValidateTypes
export const routes: Routes = [
    {
        path: '',
        component: Dashboard,
        children: [
            { path: 'multiWallet', component: MultiWallet },
            { path: 'multiWalletInfo', component: MultiWalletInfoFull },
            { path: 'mainWallet', component: WalletInfoFull }
        ]
    }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
