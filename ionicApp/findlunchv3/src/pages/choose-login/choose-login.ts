import {Component, OnInit} from '@angular/core';
import {IonicPage, NavController} from 'ionic-angular';
import {AccountPage} from "../account/account";
import {RegisterPage} from "../register/register";
import {TranslateService} from "@ngx-translate/core";

@IonicPage()
@Component({
  selector: 'page-choose-login',
  templateUrl: 'choose-login.html',
})
export class ChooseLoginPage implements OnInit {

  ngOnInit(): void {
  }

  constructor(public navCtrl: NavController,
              private translate: TranslateService) {
  }



  ionViewDidLoad() {
    console.log('ionViewDidLoad ChooseLoginPage');
  }

  goToLogin() {
    this.navCtrl.push(AccountPage);
  }

  goToRegister() {
    this.navCtrl.push(RegisterPage)
  }


}
