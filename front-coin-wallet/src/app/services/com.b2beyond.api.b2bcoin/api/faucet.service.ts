/**
 * 
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs/Observable';

import { CreateFaucetAddressRequest } from '../model/createFaucetAddressRequest';
import { FaucetAddressPayment } from '../model/faucetAddressPayment';
import { FaucetAddressRequest } from '../model/faucetAddressRequest';
import { FaucetBalanceRequest } from '../model/faucetBalanceRequest';
import { FaucetPaymentRequest } from '../model/faucetPaymentRequest';
import { RestResourceInfo } from '../model/restResourceInfo';
import { ResultOk } from '../model/resultOk';
import { UserAddress } from '../model/userAddress';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class FaucetService {

    protected basePath = 'https://api.b2bcoin.xyz/b2bcoin/api';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (let consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * create faucet address
     * Creates a faucet address and returns it
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public createFaucetAddress(body?: CreateFaucetAddressRequest, observe?: 'body', reportProgress?: boolean): Observable<UserAddress>;
    public createFaucetAddress(body?: CreateFaucetAddressRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserAddress>>;
    public createFaucetAddress(body?: CreateFaucetAddressRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserAddress>>;
    public createFaucetAddress(body?: CreateFaucetAddressRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<UserAddress>(`${this.basePath}/faucet/createFaucetAddress`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Gets the faucet address balance
     * Gets the faucet address balance and returns it
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getBalance(body?: FaucetBalanceRequest, observe?: 'body', reportProgress?: boolean): Observable<UserAddress>;
    public getBalance(body?: FaucetBalanceRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserAddress>>;
    public getBalance(body?: FaucetBalanceRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserAddress>>;
    public getBalance(body?: FaucetBalanceRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<UserAddress>(`${this.basePath}/faucet/balance`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Gets the faucet address
     * Gets the faucet address and returns it
     * @param faucetUser 
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getFaucetAddress(faucetUser: boolean, body?: FaucetAddressRequest, observe?: 'body', reportProgress?: boolean): Observable<UserAddress>;
    public getFaucetAddress(faucetUser: boolean, body?: FaucetAddressRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserAddress>>;
    public getFaucetAddress(faucetUser: boolean, body?: FaucetAddressRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserAddress>>;
    public getFaucetAddress(faucetUser: boolean, body?: FaucetAddressRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (faucetUser === null || faucetUser === undefined) {
            throw new Error('Required parameter faucetUser was null or undefined when calling getFaucetAddress.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<UserAddress>(`${this.basePath}/faucet/getFaucetAddress/${encodeURIComponent(String(faucetUser))}`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Get faucet list
     * Get faucet list and returns it
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getFaucetList(observe?: 'body', reportProgress?: boolean): Observable<Array<UserAddress>>;
    public getFaucetList(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<UserAddress>>>;
    public getFaucetList(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<UserAddress>>>;
    public getFaucetList(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];

        return this.httpClient.get<Array<UserAddress>>(`${this.basePath}/faucet/getFaucetList`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Gets the faucet address payments
     * Gets the faucet address payments and returns it
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getFaucetPayments(body?: UserAddress, observe?: 'body', reportProgress?: boolean): Observable<Array<FaucetAddressPayment>>;
    public getFaucetPayments(body?: UserAddress, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<FaucetAddressPayment>>>;
    public getFaucetPayments(body?: UserAddress, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<FaucetAddressPayment>>>;
    public getFaucetPayments(body?: UserAddress, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<Array<FaucetAddressPayment>>(`${this.basePath}/faucet/getFaucetAddressPayments`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Is the Service running ?
     * Returns if the service runs or not. Without other exceptions in mind, just running
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getResourceInfo(observe?: 'body', reportProgress?: boolean): Observable<RestResourceInfo>;
    public getResourceInfo(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<RestResourceInfo>>;
    public getResourceInfo(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<RestResourceInfo>>;
    public getResourceInfo(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
        ];

        return this.httpClient.get<RestResourceInfo>(`${this.basePath}/faucet/info`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Send a payment from the faucet
     * Send a payment from the faucet
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public payout(body?: UserAddress, observe?: 'body', reportProgress?: boolean): Observable<ResultOk>;
    public payout(body?: UserAddress, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ResultOk>>;
    public payout(body?: UserAddress, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ResultOk>>;
    public payout(body?: UserAddress, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<ResultOk>(`${this.basePath}/faucet/payout`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Send a payment from the faucet
     * Send a payment from the faucet
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public send(body?: FaucetPaymentRequest, observe?: 'body', reportProgress?: boolean): Observable<FaucetPaymentRequest>;
    public send(body?: FaucetPaymentRequest, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<FaucetPaymentRequest>>;
    public send(body?: FaucetPaymentRequest, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<FaucetPaymentRequest>>;
    public send(body?: FaucetPaymentRequest, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<FaucetPaymentRequest>(`${this.basePath}/faucet/send`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Update the faucet address
     * Update the faucet address and returns it
     * @param body 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public updateFaucetAddress(body?: UserAddress, observe?: 'body', reportProgress?: boolean): Observable<UserAddress>;
    public updateFaucetAddress(body?: UserAddress, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserAddress>>;
    public updateFaucetAddress(body?: UserAddress, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserAddress>>;
    public updateFaucetAddress(body?: UserAddress, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<UserAddress>(`${this.basePath}/faucet/updateFaucetAddress`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
