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
import { TransactionBlock } from './transactionBlock';
import { TransactionBlockWrapper } from './transactionBlockWrapper';
import { TransactionDetails } from './transactionDetails';


export interface TransactionWrapper {
    block?: TransactionBlock;
    status?: string;
    tx?: TransactionBlockWrapper;
    txDetails?: TransactionDetails;
}
