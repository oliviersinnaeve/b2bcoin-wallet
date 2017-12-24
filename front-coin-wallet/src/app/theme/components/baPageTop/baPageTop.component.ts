import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { GlobalState } from '../../../global.state';
import { UserState } from '../../../user.state';

import { TransactionsService } from '../../../pages/transactions/transactions.service';
import { WalletService } from '../../../pages/walletService.service';

import 'style-loader!./baPageTop.scss';

@Component({
    selector: 'ba-page-top',
    templateUrl: './baPageTop.html',
})
export class BaPageTop {

    public isScrolled: boolean = false;
    public isMenuCollapsed: boolean = false;


    constructor (private _state: GlobalState,
                 private userState: UserState,
                 private transactionsService: TransactionsService,
                 private walletService: WalletService,
                 private router: Router) {
        this._state.subscribe('menu.isCollapsed', (isCollapsed) => {
            this.isMenuCollapsed = isCollapsed;
        });
    }

    public selectMenuAndNotify (url, title): void {
        let breadcrumbItem = {title: title};
        this.router.navigateByUrl(url);
        this._state.notifyDataChanged('menu.activeLink', breadcrumbItem);
    }

    public toggleMenu () {
        this.isMenuCollapsed = !this.isMenuCollapsed;
        this._state.notifyDataChanged('menu.isCollapsed', this.isMenuCollapsed);
        return false;
    }

    public scrolledChanged (isScrolled) {
        this.isScrolled = isScrolled;
    }

    public logout () {
        this.userState.setUser({});
        window.location.href = "/";
    }
}
