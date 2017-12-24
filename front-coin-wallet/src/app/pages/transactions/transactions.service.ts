import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers, Response, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { NotificationsService } from 'angular2-notifications';
import 'rxjs/add/operator/catch'

import { UserState } from '../../user.state';
import * as b2bcoinModels from '../../services/com.b2beyond.api.b2bcoin/model/models';

import { languages } from '../../environment';

@Injectable()
export class TransactionsService {

    public searchUpdated:EventEmitter<any> = new EventEmitter<any>();

    public searchString: string;
    public coin: b2bcoinModels.WalletCoin;

    constructor (private http: Http,
                 private notificationsService: NotificationsService,
                 private userState: UserState) {

    }

    public triggerSearch() {
        this.searchUpdated.emit({result: "ok"});
    }

}
