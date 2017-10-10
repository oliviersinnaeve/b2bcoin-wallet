import { Injectable } from '@angular/core';
import { Http, Headers, Response, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { NotificationsService } from 'angular2-notifications';
import 'rxjs/add/operator/catch'

import { UserState } from '../../user.state';

import { languages } from '../../environment';

@Injectable()
export class PaymentService {

    constructor (private http: Http,
                 private notificationsService: NotificationsService,
                 private userState: UserState) {


    }

}
