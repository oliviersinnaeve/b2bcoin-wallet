import { Routes, RouterModule }  from '@angular/router';
import { Pages } from './pages.component';
import { ModuleWithProviders } from '@angular/core';


export const routes: Routes = [
    {
        path: 'login',
        loadChildren: 'app/pages/security/login/login.module#LoginModule'
    },
    {
        path: 'forgotPassword',
        loadChildren: 'app/pages/security/forgotPassword/forgotPassword.module#ForgotPasswordModule'
    },
    {
        path: 'resetPassword/:websiteId/:resetToken',
        loadChildren: 'app/pages/security/resetPassword/resetPassword.module#ResetPasswordModule'
    },
    {
        path: 'register',
        loadChildren: 'app/pages/security/register/register.module#RegisterModule'
    },
    {
        path: 'pages',
        component: Pages,
        children: [
            {path: '', redirectTo: 'dashboard/mainWallet', pathMatch: 'full'},
            {path: 'dashboard', loadChildren: 'app/pages/dashboard/dashboard.module#DashboardModule'},
            {path: 'coins', loadChildren: 'app/pages/coins/coins.module#CoinsModule'},
            {path: 'explorer', loadChildren: 'app/pages/explorer/explorer.module#ExplorerModule'},
            {path: 'payments', loadChildren: 'app/pages/payments/payments.module#PaymentsModule'},
            {path: 'faucet', loadChildren: 'app/pages/faucet/faucet.module#FaucetModule'},
            //{path: 'transactions', loadChildren: 'app/pages/transactions/transactions.module#TransactionsModule'},
            //{path: 'escrow', loadChildren: 'app/pages/escrow/escrow.module#EscrowModule'},
            {path: 'profile', loadChildren: 'app/pages/security/profile/profile.module#ProfileModule'},
            {path: 'settings', loadChildren: 'app/pages/security/settings/settings.module#SettingsModule'}
        ]
    }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
