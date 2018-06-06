import {Component, OnInit} from '@angular/core';
import {AlertController, IonicPage, NavController, NavParams, ToastController} from 'ionic-angular';
import {AccountPage} from "../account/account";
import {RegisterPage} from "../register/register";
import {TranslateService} from "@ngx-translate/core";
import {AuthService} from "../../shared/auth.service";
import {HttpClient} from "@angular/common/http";
import {PushService} from "../../shared/push.service";
import {LoadingService} from "../../shared/loading.service";

@IonicPage()
@Component({
  selector: 'page-choose-login',
  templateUrl: 'choose-login.html',
})
export class ChooseLoginPage implements OnInit{

  ngOnInit(): void {

  }

  constructor(public navCtrl: NavController,
              private push: PushService,
              private translate: TranslateService) {
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
