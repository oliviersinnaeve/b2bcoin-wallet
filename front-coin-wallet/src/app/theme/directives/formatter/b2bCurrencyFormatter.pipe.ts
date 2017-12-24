import { Pipe, PipeTransform } from "@angular/core";

import { WalletService } from '../../../pages/walletService.service';

@Pipe({ name: "b2bCurrency" })
export class B2BCurrencyPipe implements PipeTransform {

    private DECIMAL_SEPARATOR: string;
    private THOUSANDS_SEPARATOR: string;

    constructor(
        private walletService: WalletService
    ) {
        // TODO comes from configuration settings
        this.DECIMAL_SEPARATOR = ".";
        this.THOUSANDS_SEPARATOR = "";
    }

    transform(value: number | string): string {
        console.log("Transforming value");
        var floatValue;

        if (typeof value === "string") {
            floatValue = parseFloat(value);
        } else {
            floatValue = value;
        }

        floatValue *= this.walletService.selectedCoin.convertAmount;
        floatValue /= this.walletService.selectedCoin.convertAmount;
        return floatValue.toFixed(this.walletService.selectedCoin.fractionDigits);
    }

    parse(value: string): string {
        console.log("Parsing value");
        return ((parseFloat(value) * this.walletService.selectedCoin.convertAmount) / this.walletService.selectedCoin.convertAmount).toFixed(this.walletService.selectedCoin.fractionDigits);
    }

}