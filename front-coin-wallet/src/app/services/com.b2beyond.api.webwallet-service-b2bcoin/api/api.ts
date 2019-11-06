export * from './basicErrorController.service';
import { BasicErrorControllerService } from './basicErrorController.service';
export * from './faucetResource.service';
import { FaucetResourceService } from './faucetResource.service';
export * from './walletResource.service';
import { WalletResourceService } from './walletResource.service';
export const APIS = [BasicErrorControllerService, FaucetResourceService, WalletResourceService];
