import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";

import {HttpHeaders} from "@angular/common/http";
import {User, UserResourceService} from "./services/com.b2beyond.api.webwallet-service-user";
import {CookieService} from "angular2-cookie/core";


@Injectable()
export class UserState {

    private user: User = {};

    private handlingError: boolean = false;

    private defaultHeaders: HttpHeaders = new HttpHeaders();

    constructor (private cookieService: CookieService,
                 private userApi: UserResourceService,
                 private router: Router,
                 private route: ActivatedRoute) {
    }

    public navigateAccordingly () {
        this.user = this.cookieService.getObject("xyz");
        if (this.user !== null && this.user !== undefined && this.user.authenticationToken) {
            this.setUser(this.user);
        } else {
            this.user = {};
        }

        if (this.user.userId == undefined || this.user.userId == null || this.user.userId == "") {
            if (window.location.pathname.indexOf("resetPassword") == -1 && window.location.hash.indexOf("resetPassword") == -1) {
                this.router.navigateByUrl("login");
            }
        }

        this.defaultHeaders.append("Content-Type", "application/json");
    }

    public handleError (error, callback, context) {
        if (!this.handlingError) {
            this.handlingError = true;
            this.user = null;
            let tmpUser: User = JSON.parse(this.cookieService.get("xyz"));
            this.defaultHeaders.delete("Authorization");
            this.defaultHeaders.append("Authorization", tmpUser.authenticationRefreshToken);

            this.userApi.refreshUsingPOST(tmpUser).subscribe(
                (result) => {
                    this.setUser(result);
                    callback.call(context);
                },
                (error) => {
                    this.router.navigateByUrl("login");
                }
            )
        } else {
            //if (this.user !== null) {
                console.log("Recalling function");
                callback.call(context);
                this.handlingError = false;
            //}
        }
    }

    public setUser (user: User) {
        this.cookieService.put("xyz", JSON.stringify(user));
        this.user = user;
        this.defaultHeaders.delete("Authorization");
        this.defaultHeaders.append("Authorization", user.authenticationToken);
    }

    public getUser (): User {
        return this.user;
    }

    public getExtraHeaders (): HttpHeaders {
        return this.defaultHeaders;
    }

}
