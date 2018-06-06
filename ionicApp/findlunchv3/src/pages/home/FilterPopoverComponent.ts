import {Component} from "@angular/core";
import {FilterPopoverService} from "../home/FilterPopoverService";
import {AuthService} from "../../shared/auth.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
    templateUrl: "../home/FilterPopoverComponent.html"
})
export class FilterPopoverComponent {

    constructor(public service: FilterPopoverService,
                public auth: AuthService,
                public translate: TranslateService) {
    }
}
