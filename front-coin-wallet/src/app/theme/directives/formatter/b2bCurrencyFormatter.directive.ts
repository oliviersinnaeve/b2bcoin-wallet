import { Directive, HostListener, ElementRef, OnInit } from "@angular/core";
import { B2BCurrencyPipe } from "./b2bCurrencyFormatter.pipe";

@Directive({ selector: "[b2bCurrencyFormatter]" })
export class B2BCurrencyFormatterDirective implements OnInit {

    private el: HTMLInputElement;

    constructor(
        private elementRef: ElementRef,
        private currencyPipe: B2BCurrencyPipe
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