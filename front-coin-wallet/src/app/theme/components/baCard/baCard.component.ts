import {Component, Input} from '@angular/core';

@Component({
    selector: 'ba-card',
    templateUrl: './baCard.html',
})
export class BaCard {

    @Input() title: String;
    @Input() baCardClass: String;
    @Input() cardType: String;
    @Input() collapsable: boolean;
    @Input() showContent: boolean;

    public hideBody () {
        this.showContent = false;
    }

    public showBody () {
        this.showContent = true;
    }

}
