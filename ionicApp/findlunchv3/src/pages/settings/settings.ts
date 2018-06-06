import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform } from 'ionic-angular';
import {SERVER_URL} from '../../app/app.module';
import {InAppBrowser} from '@ionic-native/in-app-browser';
/**
 * Generated class for the SettingsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-settings',
  templateUrl: 'settings.html',
})
export class SettingsPage {

  constructor(public platform: Platform, public navCtrl: NavController, public navParams: NavParams, private iab: InAppBrowser) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SettingsPage');
  }

  /**
   * Opens a url in the inapp browser
   * @param url
   */
  public openUrl(url: string): void {
    console.log('im OpenURL');
    if (url !== null) {
      this.platform.ready().then(() => {
        this.iab.create(url, "location=yes");
      });
    }
  }
  /**
  * opens in app browser on terms&conditions url
  */

  public goToTermsAndConditions(): void {
    this.openUrl(`${SERVER_URL}/terms`);
  }

  /**
   * opens in app browser on faq url
   * */

  public goToFaQ(): void {
    this.openUrl(`${SERVER_URL}/faq_customer`);
  }



}
