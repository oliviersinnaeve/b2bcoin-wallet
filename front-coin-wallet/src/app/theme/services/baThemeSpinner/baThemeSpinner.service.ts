import {Injectable} from '@angular/core';

@Injectable()
export class BaThemeSpinner {

    private _selector: string = 'preloader';
    private _element: HTMLElement;

    private interval: number;

    constructor () {
        this._element = document.getElementById(this._selector);

        //let startIndex = 1;
        //this.interval = setInterval(function() {
        //    let image: HTMLElement = document.getElementById("loader_" + startIndex);
        //    console.log("Current image", image);
        //
        //    let newImage: HTMLElement = document.getElementById("loader_" + (startIndex + 1));
        //    image.classList.add("hidden");
        //    newImage.classList.remove("hidden");
        //
        //    startIndex += 1;
        //}, 33);
    }

    public show (): void {
        this._element.style['display'] = 'block';
    }

    public hide (delay: number = 0): void {
        setTimeout(() => {
            this._element.style['display'] = 'none';
            clearInterval(this.interval);
        }, delay);
    }
}
