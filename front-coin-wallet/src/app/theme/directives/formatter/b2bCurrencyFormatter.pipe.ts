import { Pipe, PipeTransform } from "@angular/core";


@Pipe({ name: "b2bCurrency" })
export class B2BCurrencyPipe implements PipeTransform {

    private DECIMAL_SEPARATOR: string;
    private THOUSANDS_SEPARATOR: string;

    constructor() {
        // TODO comes from configuration settings
        this.DECIMAL_SEPARATOR = ".";
        this.THOUSANDS_SEPARATOR = "";
    }

    transform(value: number | string, fractionSize: number = 12): string {
        console.log("Transforming value");
        return ((value * 1000000000000) / 1000000000000).toFixed(fractionSize);
    }

    parse(value: string, fractionSize: number = 12): string {
        console.log("Parsing value");
        return ((value * 1000000000000) / 1000000000000).toFixed(fractionSize);
    }

}