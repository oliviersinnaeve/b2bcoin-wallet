import { Component, ViewChild } from '@angular/core';
import { Router } from "@angular/router";
import { Http, Headers, RequestOptionsArgs, Response, URLSearchParams } from '@angular/http';
import { ModalDirective } from 'ngx-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { UserState } from '../../../../user.state';

import { WalletService } from '../../../walletService.service';

import * as b2bcoinModels from '../../../../services/com.b2beyond.api.b2bcoin/model/models';
import { WalletApi } from '../../../../services/com.b2beyond.api.b2bcoin/api/WalletApi';

import { websiteId } from '../../../../environment';

@Component({
    selector: 'overview',
    templateUrl: './overview.html',
})

export class Overview {

    public payments: Array<any> = [];

    constructor (private notificationsService: NotificationsService,
                 private userState: UserState,
                 private walletApi: WalletApi,
                 private walletService: WalletService,
                 private http: Http,
                 private router: Router) {

        this.walletApi.defaultHeaders = userState.getExtraHeaders();
    }

    public ngAfterViewInit (): void {
        //if (this.componentService.hasComponentFragment()) {
        //  this.componentFragment = this.componentService.getComponentFragment();
        //
        //  for (var i = 0; i < this.componentFragment.designs.length; i++) {
        //    if (this.componentFragment.designs[i].designId == this.componentFragment.selectedDesignId) {
        //      this.componentService.selectedDesign = this.componentFragment.designs[i];
        //    }
        //  }
        //
        //  let contentTagName = this.componentFragment.name.toLowerCase().replace(/ /g, "-") + "-content";
        //  this.htmlTags.push({name: contentTagName, description: "Add <" + contentTagName + "/> to view The components content"});
        //
        //  if (this.componentFragment.serviceUrl != undefined && this.componentFragment.serviceUrl != null && this.componentFragment.serviceUrl != "") {
        //    this.getServicePropertiesFormSkeleton();
        //    this.getServicePropertiesSkeleton(false);
        //    // this.getServiceBusinessPropertiesFormSkeleton();
        //    // this.getServiceBusinessPropertiesSkeleton(false);
        //
        //    this.linkedService = true;
        //  }
        //  this.componentpropertyApi.getFragmentsForSearch({
        //    user: this.componentFragment.user,
        //    websiteId: this.componentFragment.websiteId,
        //    pageId: 0,
        //    componentId: this.componentFragment.componentId
        //  }).subscribe(result => {
        //    this.componentProperties = result;
        //  });
        //} else {
        //  this.router.navigateByUrl("pages/components/public");
        //}
    }

}
