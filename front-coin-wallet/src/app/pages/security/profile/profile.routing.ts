import { Routes, RouterModule }  from '@angular/router';

import { Profile } from './profile.component';

// noinspection TypeScriptValidateTypes
const routes: Routes = [
    {
        path: '',
        component: Profile
    }
];

export const routing = RouterModule.forChild(routes);
