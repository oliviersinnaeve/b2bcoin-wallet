import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Configuration } from './configuration';

import { FaucetService } from './api/faucet.service';
import { WalletService } from './api/wallet.service';

@NgModule({
  imports:      [ CommonModule, HttpClientModule ],
  declarations: [],
  exports:      [],
  providers: [
    FaucetService,
    WalletService ]
})
export class ApiModuleCoin {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModuleCoin,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        }
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModuleCoin) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import your base AppModule only.');
        }
    }
}
