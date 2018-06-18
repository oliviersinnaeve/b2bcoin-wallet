export * from './user.service';
import { UserService } from './user.service';
export * from './userrole.service';
import { UserroleService } from './userrole.service';
export const APIS = [UserService, UserroleService];
