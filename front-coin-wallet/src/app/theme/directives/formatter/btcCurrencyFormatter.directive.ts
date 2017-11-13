import { Directive, HostListener, ElementRef, OnInit } from "@angular/core";
import { BTCCurrencyPipe } from "./btcCurrencyFormatter.pipe";

@Directive({ selector: "[btcCurrencyFormatter]" })
export class BTCCurrencyFormatterDirective implements OnInit {

    private el: HTMLInputElement;

    constructor(
        private elementRef: ElementRef,
        private currencyPipe: BTCCurrencyPipe
    ) {
        this.el = this.elementRef.nativeElement;
    }

    ngOnInit() {
        this.el.value = this.currencyPipe.transform(this.el.value);
    }

    @HostListener("focus", ["$event.target.value"])
    onFocus(value) {
        console.log("focus on field");
        this.el.value = this.currencyPipe.parse(value); // opossite of transform
    }

    @HostListener("blur", ["$event.target.value"])
    onBlur(value) {
        console.log("blur on field");
        this.el.value = this.currencyPipe.transform(value);
    }

}