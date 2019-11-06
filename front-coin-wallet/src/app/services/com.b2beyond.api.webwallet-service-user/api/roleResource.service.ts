/**
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
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

import { Observable }                                        from 'rxjs';

import { RestResourceInfo } from '../model/restResourceInfo';
import { User } from '../model/user';
import { UserRole } from '../model/userRole';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class RoleResourceService {

    protected basePath = 'https://localhost';
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
     * Add the user role by ID
     * Add the user role, returns a boolean
     * @param search search
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public addUserRoleUsingPOST(search: UserRole, observe?: 'body', reportProgress?: boolean): Observable<UserRole>;
    public addUserRoleUsingPOST(search: UserRole, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserRole>>;
    public addUserRoleUsingPOST(search: UserRole, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserRole>>;
    public addUserRoleUsingPOST(search: UserRole, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (search === null || search === undefined) {
            throw new Error('Required parameter search was null or undefined when calling addUserRoleUsingPOST.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            '*/*'
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

        return this.httpClient.post<UserRole>(`${this.basePath}/user/roles/add`,
            search,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Delete the user role by ID
     * Deletes the user role, returns a boolean
     * @param search search
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteUserRoleUsingPOST(search: UserRole, observe?: 'body', reportProgress?: boolean): Observable<boolean>;
    public deleteUserRoleUsingPOST(search: UserRole, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<boolean>>;
    public deleteUserRoleUsingPOST(search: UserRole, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<boolean>>;
    public deleteUserRoleUsingPOST(search: UserRole, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (search === null || search === undefined) {
            throw new Error('Required parameter search was null or undefined when calling deleteUserRoleUsingPOST.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            '*/*'
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

        return this.httpClient.post<boolean>(`${this.basePath}/user/roles/delete`,
            search,
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
    public getResourceInfoUsingGET(observe?: 'body', reportProgress?: boolean): Observable<RestResourceInfo>;
    public getResourceInfoUsingGET(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<RestResourceInfo>>;
    public getResourceInfoUsingGET(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<RestResourceInfo>>;
    public getResourceInfoUsingGET(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            '*/*'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
        ];

        return this.httpClient.get<RestResourceInfo>(`${this.basePath}/user/roles/info`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Add the user role by ID
     * Add the user role, returns a boolean
     * @param search search
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getUserRolesUsingPOST(search: User, observe?: 'body', reportProgress?: boolean): Observable<boolean>;
    public getUserRolesUsingPOST(search: User, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<boolean>>;
    public getUserRolesUsingPOST(search: User, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<boolean>>;
    public getUserRolesUsingPOST(search: User, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (search === null || search === undefined) {
            throw new Error('Required parameter search was null or undefined when calling getUserRolesUsingPOST.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            '*/*'
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

        return this.httpClient.post<boolean>(`${this.basePath}/user/roles/get`,
            search,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}