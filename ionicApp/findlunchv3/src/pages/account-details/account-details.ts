 import {Component, ViewChild} from '@angular/core';
import {IonicPage, Nav, NavController, Toast, ToastController} from 'ionic-angular';
import {AuthService} from '../../shared/auth.service';
import {QRService} from "../../pages/bonus/qr.service";
import {TranslateService} from "@ngx-translate/core";
import {APP_LANG} from "../../app/app.module";
import {BonusPage} from "../bonus/bonus";
import {ReservationsPage} from "../reservations/reservations";
import {HomePage} from "../home/home";

/**
 * Generated class for the AccountDetailsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()

@Component({
  selector: 'page-account-details',
  templateUrl: 'account-details.html',
})
export class AccountDetailsPage {
  @ViewChild(Nav) public nav: Nav;

  rootPage: any = HomePage;
  private strLogoutSuccess: string;

  constructor(public navCtrl: NavController,
              public auth: AuthService,
              public toastCtrl: ToastController,
              public qr: QRService,
              private translate: TranslateService) {
    translate.setDefaultLang(APP_LANG);
    this.auth.verifyUser();

    document.addEventListener('resume', () => {
      this.auth.verifyUser();
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AccountDetailsPage');

  }
  public ngOnInit(): void {
    this.translate.get('Success.logoutSuccess').subscribe(
      (value: string) => {
        this.strLogoutSuccess = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Success.logoutSuccess.", err);
      }
    );
  }
  /**
   * opens the clicked page. Reset the content nav to have just this page.
   * @param page
   *  the page the user clicked
   */

  openBonusPage() {
    this.navCtrl.push(BonusPage);
  }

  openReservationsPage(){
    this.navCtrl.push(ReservationsPage);
  }

  logout(){
    this.auth.logout();

    const toast: Toast = this.toastCtrl.create({
      message: this.strLogoutSuccess,
      duration: 3000
    });
    toast.present();

    this.navCtrl.pop();
  }
}
