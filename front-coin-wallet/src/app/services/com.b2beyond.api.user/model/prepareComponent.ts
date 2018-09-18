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
import { LoginProperties } from './loginProperties';
import { Translation } from './translation';


export interface PrepareComponent {
    preview?: boolean;
    content?: string;
    translations?: Array<Translation>;
    dataSets?: { [key: string]: string; };
    properties?: LoginProperties;
}