import {EventEmitter, Injectable} from '@angular/core';
import {NotificationsService} from 'angular2-notifications';
import 'rxjs/add/operator/catch'

import {UserState} from '../../user.state';
import * as b2bcoinModels from '../../services/com.b2beyond.api.webwallet-service-b2bcoin/model/models';

@Injectable()
export class TransactionsService {

    public searchUpdated:EventEmitter<any> = new EventEmitter<any>();

    public searchString: string;
    public coin: b2bcoinModels.WalletCoin;

    constructor (private notificationsService: NotificationsService,
                 private userState: UserState) {

    }

    public triggerSearch() {
        this.searchUpdated.emit({result: "ok"});
    }

}
