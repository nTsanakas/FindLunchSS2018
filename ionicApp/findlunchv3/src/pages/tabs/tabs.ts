import { Component } from '@angular/core';

import { HomePage } from '../home/home';
import { SettingsPage } from '../settings/settings';
import {AuthService} from "../../shared/auth.service";
import {AccountDetailsPage} from "../account-details/account-details";
import {ChooseLoginPage} from "../choose-login/choose-login";

@Component({
  selector: 'tabs-page',
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = ChooseLoginPage;
  tab2Root = HomePage;
  tab3Root = SettingsPage;
  tab4Root = AccountDetailsPage;

  constructor(private auth: AuthService) {
  }
}
