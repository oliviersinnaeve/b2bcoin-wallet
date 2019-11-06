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
import { Address } from './address';


export interface PaymentInput {
    address?: string;
    addresses?: Array<Address>;
    anonymity?: number;
    fee?: number;
    params?: string;
    paymentId?: string;
    transfers?: { [key: string]: number; };
    unlockTime?: number;
}