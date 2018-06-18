export * from './faucet.service';
import { FaucetService } from './faucet.service';
export * from './wallet.service';
import { WalletService } from './wallet.service';
export const APIS = [FaucetService, WalletService];
