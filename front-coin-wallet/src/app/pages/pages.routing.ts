import { Routes, RouterModule }  from '@angular/router';
import { Pages } from './pages.component';
import { ModuleWithProviders } from '@angular/core';


export const routes: Routes = [
    {
        path: 'login',
        loadChildren: './security/login/login.module#LoginModule'
    },
    {
        path: 'forgotPassword',
        loadChildren: './security/forgotPassword/forgotPassword.module#ForgotPasswordModule'
    },
    {
        path: 'resetPassword/:websiteId/:resetToken',
        loadChildren: './security/resetPassword/resetPassword.module#ResetPasswordModule'
    },
    {
        path: 'register',
        loadChildren: './security/register/register.module#RegisterModule'
    },
    {
        path: 'pages',
        component: Pages,
        children: [
            {path: '', redirectTo: 'dashboard/mainWallet', pathMatch: 'full'},
            {path: 'dashboard', loadChildren: './dashboard/dashboard.module#DashboardModule'},
            {path: 'coins', loadChildren: './coins/coins.module#CoinsModule'},
            {path: 'explorer', loadChildren: './explorer/explorer.module#ExplorerModule'},
            {path: 'payments', loadChildren: './payments/payments.module#PaymentsModule'},
            {path: 'faucet', loadChildren: './faucet/faucet.module#FaucetModule'},
            //{path: 'transactions', loadChildren: './transactions/transactions.module#TransactionsModule'},
            //{path: 'escrow', loadChildren: './escrow/escrow.module#EscrowModule'},
            {path: 'profile', loadChildren: './security/profile/profile.module#ProfileModule'},
            {path: 'settings', loadChildren: './security/settings/settings.module#SettingsModule'}
        ]
    }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
