

import {Component, OnInit} from "@angular/core";
import {Alert, AlertController, Loading, NavController, NavParams, Toast, ToastController, IonicPage} from "ionic-angular";
import {AuthService} from "../../shared/auth.service";
import {SERVER_URL} from "../../app/app.module";
import {LoadingService} from "../../shared/loading.service";
import {AccountDetailsPage} from "../account-details/account-details";
import {TranslateService} from '@ngx-translate/core';
import {PushService} from "../../shared/push.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../../model/User";


/**
 * Generated class for the AccountPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-account',
  templateUrl: 'account.html',

})
export class AccountPage implements OnInit{

  private goBack: boolean;
  private counterPasswordWrong: number = 0;
  private strLoginError: string;
  private strLoginSuccessful: string;
  private strConnectionError: string;
  private strPasswordResetSuccess: string;
  private strError: string;

  constructor(public navCtrl: NavController,
              private toastCtrl: ToastController,
              private auth: AuthService,
              private http: HttpClient,
              private push: PushService,
              private alertCtrl: AlertController,
              private loading: LoadingService,
              private translate: TranslateService
              ) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AccountPage');
  }

  public ngOnInit(): void {
    this.translate.get('Error.login').subscribe(
      (value: string) => {
        this.strLoginError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.login.", err);
      });
    this.translate.get('Error.general').subscribe(
      (value: string) => {
        this.strError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.general.", err);
      });
    this.translate.get('Success.login').subscribe(
      (value: string) => {
        this.strLoginSuccessful = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Success.login.", err);
      });
    this.translate.get('Error.connection').subscribe(
      (value: string) => {
        this.strConnectionError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.connection.", err);
      });
    this.translate.get('Success.passwordReset').subscribe(
      (value: string) => {
        this.strPasswordResetSuccess = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Success.passwordReset.", err);
      });
  }
  onGoBackAction(){
      this.navCtrl.pop();
  }
  /**
   * Logs the user in. LoggedIn is stateless (without session etc.).
   * If the authentication fails it activates the password reset button.
   *
   * @param userName
   * @param password
   */
  onLogInAction(userName: string, password: string): void {
    const user: User = {
      username: userName,
      password: password
    };


    const encodedCredentials: string = btoa(`${userName}:${password}`);
    const loader: Loading = this.loading.prepareLoader();
    loader.present().then(() => {
      this.auth.login(userName, password)
        .subscribe(
          resp  => {
            window.localStorage.setItem("username", userName);
            window.localStorage.setItem(userName, encodedCredentials);
            this.auth.userName = window.localStorage.getItem("username");
            this.auth.loggedIn = true;

              const toast: Toast = this.toastCtrl.create({
                message: this.strLoginSuccessful,
                duration: 3000
              });
              toast.present();
              this.push.pushSetup(user);
              // When comeBack is true, after login user is sent back to the view he came from
              if (this.goBack) {
                this.navCtrl.pop();
                loader.dismiss();

              } else {
                // else go to the accountdetails
                this.navCtrl.push(AccountDetailsPage);
                console.log("Opening AccountDetailsPage");
                loader.dismiss();
              }
          },
          err => {
            this.counterPasswordWrong++;
            loader.dismiss();
            console.error("Login failed!", err);
            const alert: Alert = this.alertCtrl.create({
              title: this.strError,
              message: this.strLoginError,
              buttons: [{
                text: 'Ok',
                role: 'cancel'
              }]
            });
            alert.present();
          }
        );
    });

  }

  /**
   * Checks whether there is a string in the user name field and whether password was
   * entered wrong
   * @param username = email address of user
   */
  public isEmptyUser(username: string): boolean {
    return !(username && this.counterPasswordWrong >= 1);
  }

  /**
   * Requesting a password reset by the backend
   * @param username = email adress of user
   */
  public sendPasswordReset(username: string): void {
    const user: any = {username: username};

    const headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
        body: JSON.stringify(user)
    });

    const loader: Loading = this.loading.prepareLoader();
    loader.present().then(() => {
      this.http.get(`${SERVER_URL}/api/get_reset_token`, {headers}, )
        .timeout(8000)
        .subscribe(
          res => {
            let msg: string;
            switch (res) {
              case 0:
                msg = this.strPasswordResetSuccess;
                break;
              default:
                msg = this.strConnectionError;
                break;
            }
            const toast: Toast = this.toastCtrl.create({
              message: msg,
              duration: 3000
            });
            loader.dismiss();
            toast.present();
          },
          err => {
            loader.dismiss();
            console.error(err);
            const alert: Alert = this.alertCtrl.create({
              title: this.strError,
              message: this.strConnectionError,
              buttons: [{
                text: 'Ok',
                role: 'cancel'
              }]
            });
            alert.present();
          }
        );
    });
  }


}
