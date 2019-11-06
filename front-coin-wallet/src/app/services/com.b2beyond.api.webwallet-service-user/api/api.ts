export * from './basicErrorController.service';
import { BasicErrorControllerService } from './basicErrorController.service';
export * from './roleResource.service';
import { RoleResourceService } from './roleResource.service';
export * from './userResource.service';
import { UserResourceService } from './userResource.service';
export const APIS = [BasicErrorControllerService, RoleResourceService, UserResourceService];
