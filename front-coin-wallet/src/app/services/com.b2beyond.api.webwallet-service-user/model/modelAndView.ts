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
import { View } from './view';


export interface ModelAndView {
    empty?: boolean;
    model?: any;
    modelMap?: { [key: string]: any; };
    reference?: boolean;
    status?: ModelAndView.StatusEnum;
    view?: View;
    viewName?: string;
}
export namespace ModelAndView {
    export type StatusEnum = '100 CONTINUE' | '101 SWITCHING_PROTOCOLS' | '102 PROCESSING' | '103 CHECKPOINT' | '200 OK' | '201 CREATED' | '202 ACCEPTED' | '203 NON_AUTHORITATIVE_INFORMATION' | '204 NO_CONTENT' | '205 RESET_CONTENT' | '206 PARTIAL_CONTENT' | '207 MULTI_STATUS' | '208 ALREADY_REPORTED' | '226 IM_USED' | '300 MULTIPLE_CHOICES' | '301 MOVED_PERMANENTLY' | '302 FOUND' | '302 MOVED_TEMPORARILY' | '303 SEE_OTHER' | '304 NOT_MODIFIED' | '305 USE_PROXY' | '307 TEMPORARY_REDIRECT' | '308 PERMANENT_REDIRECT' | '400 BAD_REQUEST' | '401 UNAUTHORIZED' | '402 PAYMENT_REQUIRED' | '403 FORBIDDEN' | '404 NOT_FOUND' | '405 METHOD_NOT_ALLOWED' | '406 NOT_ACCEPTABLE' | '407 PROXY_AUTHENTICATION_REQUIRED' | '408 REQUEST_TIMEOUT' | '409 CONFLICT' | '410 GONE' | '411 LENGTH_REQUIRED' | '412 PRECONDITION_FAILED' | '413 PAYLOAD_TOO_LARGE' | '413 REQUEST_ENTITY_TOO_LARGE' | '414 URI_TOO_LONG' | '414 REQUEST_URI_TOO_LONG' | '415 UNSUPPORTED_MEDIA_TYPE' | '416 REQUESTED_RANGE_NOT_SATISFIABLE' | '417 EXPECTATION_FAILED' | '418 I_AM_A_TEAPOT' | '419 INSUFFICIENT_SPACE_ON_RESOURCE' | '420 METHOD_FAILURE' | '421 DESTINATION_LOCKED' | '422 UNPROCESSABLE_ENTITY' | '423 LOCKED' | '424 FAILED_DEPENDENCY' | '426 UPGRADE_REQUIRED' | '428 PRECONDITION_REQUIRED' | '429 TOO_MANY_REQUESTS' | '431 REQUEST_HEADER_FIELDS_TOO_LARGE' | '451 UNAVAILABLE_FOR_LEGAL_REASONS' | '500 INTERNAL_SERVER_ERROR' | '501 NOT_IMPLEMENTED' | '502 BAD_GATEWAY' | '503 SERVICE_UNAVAILABLE' | '504 GATEWAY_TIMEOUT' | '505 HTTP_VERSION_NOT_SUPPORTED' | '506 VARIANT_ALSO_NEGOTIATES' | '507 INSUFFICIENT_STORAGE' | '508 LOOP_DETECTED' | '509 BANDWIDTH_LIMIT_EXCEEDED' | '510 NOT_EXTENDED' | '511 NETWORK_AUTHENTICATION_REQUIRED';
    export const StatusEnum = {
        _100CONTINUE: '100 CONTINUE' as StatusEnum,
        _101SWITCHINGPROTOCOLS: '101 SWITCHING_PROTOCOLS' as StatusEnum,
        _102PROCESSING: '102 PROCESSING' as StatusEnum,
        _103CHECKPOINT: '103 CHECKPOINT' as StatusEnum,
        _200OK: '200 OK' as StatusEnum,
        _201CREATED: '201 CREATED' as StatusEnum,
        _202ACCEPTED: '202 ACCEPTED' as StatusEnum,
        _203NONAUTHORITATIVEINFORMATION: '203 NON_AUTHORITATIVE_INFORMATION' as StatusEnum,
        _204NOCONTENT: '204 NO_CONTENT' as StatusEnum,
        _205RESETCONTENT: '205 RESET_CONTENT' as StatusEnum,
        _206PARTIALCONTENT: '206 PARTIAL_CONTENT' as StatusEnum,
        _207MULTISTATUS: '207 MULTI_STATUS' as StatusEnum,
        _208ALREADYREPORTED: '208 ALREADY_REPORTED' as StatusEnum,
        _226IMUSED: '226 IM_USED' as StatusEnum,
        _300MULTIPLECHOICES: '300 MULTIPLE_CHOICES' as StatusEnum,
        _301MOVEDPERMANENTLY: '301 MOVED_PERMANENTLY' as StatusEnum,
        _302FOUND: '302 FOUND' as StatusEnum,
        _302MOVEDTEMPORARILY: '302 MOVED_TEMPORARILY' as StatusEnum,
        _303SEEOTHER: '303 SEE_OTHER' as StatusEnum,
        _304NOTMODIFIED: '304 NOT_MODIFIED' as StatusEnum,
        _305USEPROXY: '305 USE_PROXY' as StatusEnum,
        _307TEMPORARYREDIRECT: '307 TEMPORARY_REDIRECT' as StatusEnum,
        _308PERMANENTREDIRECT: '308 PERMANENT_REDIRECT' as StatusEnum,
        _400BADREQUEST: '400 BAD_REQUEST' as StatusEnum,
        _401UNAUTHORIZED: '401 UNAUTHORIZED' as StatusEnum,
        _402PAYMENTREQUIRED: '402 PAYMENT_REQUIRED' as StatusEnum,
        _403FORBIDDEN: '403 FORBIDDEN' as StatusEnum,
        _404NOTFOUND: '404 NOT_FOUND' as StatusEnum,
        _405METHODNOTALLOWED: '405 METHOD_NOT_ALLOWED' as StatusEnum,
        _406NOTACCEPTABLE: '406 NOT_ACCEPTABLE' as StatusEnum,
        _407PROXYAUTHENTICATIONREQUIRED: '407 PROXY_AUTHENTICATION_REQUIRED' as StatusEnum,
        _408REQUESTTIMEOUT: '408 REQUEST_TIMEOUT' as StatusEnum,
        _409CONFLICT: '409 CONFLICT' as StatusEnum,
        _410GONE: '410 GONE' as StatusEnum,
        _411LENGTHREQUIRED: '411 LENGTH_REQUIRED' as StatusEnum,
        _412PRECONDITIONFAILED: '412 PRECONDITION_FAILED' as StatusEnum,
        _413PAYLOADTOOLARGE: '413 PAYLOAD_TOO_LARGE' as StatusEnum,
        _413REQUESTENTITYTOOLARGE: '413 REQUEST_ENTITY_TOO_LARGE' as StatusEnum,
        _414URITOOLONG: '414 URI_TOO_LONG' as StatusEnum,
        _414REQUESTURITOOLONG: '414 REQUEST_URI_TOO_LONG' as StatusEnum,
        _415UNSUPPORTEDMEDIATYPE: '415 UNSUPPORTED_MEDIA_TYPE' as StatusEnum,
        _416REQUESTEDRANGENOTSATISFIABLE: '416 REQUESTED_RANGE_NOT_SATISFIABLE' as StatusEnum,
        _417EXPECTATIONFAILED: '417 EXPECTATION_FAILED' as StatusEnum,
        _418IAMATEAPOT: '418 I_AM_A_TEAPOT' as StatusEnum,
        _419INSUFFICIENTSPACEONRESOURCE: '419 INSUFFICIENT_SPACE_ON_RESOURCE' as StatusEnum,
        _420METHODFAILURE: '420 METHOD_FAILURE' as StatusEnum,
        _421DESTINATIONLOCKED: '421 DESTINATION_LOCKED' as StatusEnum,
        _422UNPROCESSABLEENTITY: '422 UNPROCESSABLE_ENTITY' as StatusEnum,
        _423LOCKED: '423 LOCKED' as StatusEnum,
        _424FAILEDDEPENDENCY: '424 FAILED_DEPENDENCY' as StatusEnum,
        _426UPGRADEREQUIRED: '426 UPGRADE_REQUIRED' as StatusEnum,
        _428PRECONDITIONREQUIRED: '428 PRECONDITION_REQUIRED' as StatusEnum,
        _429TOOMANYREQUESTS: '429 TOO_MANY_REQUESTS' as StatusEnum,
        _431REQUESTHEADERFIELDSTOOLARGE: '431 REQUEST_HEADER_FIELDS_TOO_LARGE' as StatusEnum,
        _451UNAVAILABLEFORLEGALREASONS: '451 UNAVAILABLE_FOR_LEGAL_REASONS' as StatusEnum,
        _500INTERNALSERVERERROR: '500 INTERNAL_SERVER_ERROR' as StatusEnum,
        _501NOTIMPLEMENTED: '501 NOT_IMPLEMENTED' as StatusEnum,
        _502BADGATEWAY: '502 BAD_GATEWAY' as StatusEnum,
        _503SERVICEUNAVAILABLE: '503 SERVICE_UNAVAILABLE' as StatusEnum,
        _504GATEWAYTIMEOUT: '504 GATEWAY_TIMEOUT' as StatusEnum,
        _505HTTPVERSIONNOTSUPPORTED: '505 HTTP_VERSION_NOT_SUPPORTED' as StatusEnum,
        _506VARIANTALSONEGOTIATES: '506 VARIANT_ALSO_NEGOTIATES' as StatusEnum,
        _507INSUFFICIENTSTORAGE: '507 INSUFFICIENT_STORAGE' as StatusEnum,
        _508LOOPDETECTED: '508 LOOP_DETECTED' as StatusEnum,
        _509BANDWIDTHLIMITEXCEEDED: '509 BANDWIDTH_LIMIT_EXCEEDED' as StatusEnum,
        _510NOTEXTENDED: '510 NOT_EXTENDED' as StatusEnum,
        _511NETWORKAUTHENTICATIONREQUIRED: '511 NETWORK_AUTHENTICATION_REQUIRED' as StatusEnum
    }
}
