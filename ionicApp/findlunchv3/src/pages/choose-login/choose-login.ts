import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import {AccountPage} from "../account/account";
import {RegisterPage} from "../register/register";
import {TranslateService} from "@ngx-translate/core";

@IonicPage()
@Component({
  selector: 'page-choose-login',
  templateUrl: 'choose-login.html',
})
export class ChooseLoginPage {

  constructor(public navCtrl: NavController, public navParams: NavParams, public translate: TranslateService) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ChooseLoginPage');
  }
  goToLogin(){
    this.navCtrl.push(AccountPage);
  }
  goToRegister(){
    this.navCtrl.push(RegisterPage)
  }


}
