import {Component} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {UserState} from '../../../user.state';

import {User, UserResourceService} from "../../../services/com.b2beyond.api.webwallet-service-user";


@Component({
    selector: 'profile',
    templateUrl: './profile.html',
    styleUrls: ['./profile.scss']
})
export class Profile {

    public messages: Array<string> = [];
    public errors: Array<string> = [];

    public user: User;
    public userImage: any = "//placehold.it/100";

    public submitted: boolean = false;

    constructor (private fb: FormBuilder,
                 private userApi: UserResourceService,
                 private userState: UserState) {

        this.initialize();
    }

    private initialize() {
        this.user = this.userState.getUser();
    }

    public readProfileFile(event: any) {
        if (event.target.files && event.target.files[0]) {
            var reader = new FileReader();

            reader.onload = (event: any) => {
                this.userImage = event.target.result;

                console.log("User image", this.userImage);
            };

            reader.readAsDataURL(event.target.files[0]);
        }
    }

}
