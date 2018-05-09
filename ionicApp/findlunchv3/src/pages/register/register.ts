
import {Component} from "@angular/core";
import {Alert, AlertController, Loading, NavController, NavParams, Toast, ToastController, IonicPage} from "ionic-angular";
import {AuthService} from "../../shared/auth.service";
import {InAppBrowser} from "@ionic-native/in-app-browser";
import {LoadingService} from "../../shared/loading.service";
import {TranslateService} from "@ngx-translate/core";
import {SERVER_URL} from "../../app/app.module";
import {PushService} from "../../shared/push.service";
import {TabsPage} from "../tabs/tabs";
import {User} from "../../model/User";

var emailRegEX = new RegExp("^[A-Za-zÄÖÜäöü0-9.!#$%&'*+-/=?^_`{|}~]+@[A-Za-zÄÖÜäöü0-9]+\\.[A-Za-z]{2,4}$")
var passwordLowercaseRegEx = new RegExp("[a-zäöü]")
var passwordUppercaseRegEx = new RegExp("[A-ZÄÖÜ]")
var passwordDigitRegEx = new RegExp("[0-9]")
var passwordSpecialRegEx = new RegExp("[<>|,;.:\\-_#'+*~°!§$%&/()=?\"]")
var passwordLengthRegEx = new RegExp(".{5,}")

/**
 * Generated class for the RegisterPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register',
  templateUrl: 'register.html',
})
export class RegisterPage {


  private termsAndConditionsChecked: boolean;
  private goBack: boolean;

  private strNoValidEmail: string;
  private strNoValidPassword: string;
  private strUsedEmail: string;
  private strConnectionError: string;
  private strConfirmPasswordError: string;
  private strTermsAndConditionError: string;
  private strRegisterSuccess: string;
  private strError: string;

  constructor(private auth: AuthService,
              private toastCtrl: ToastController,
              private navCtrl: NavController,
              private navParams: NavParams,
              private iab: InAppBrowser,
              private alertCtrl: AlertController,
              private loading: LoadingService,
              private translate: TranslateService,
              private push: PushService
              ) {
    this.goBack = navParams.get("comeBack");
    this.termsAndConditionsChecked = false;
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');
  }
  onGoBackAction(){
    this.navCtrl.pop();
  }

  public ngOnInit(): void {
    this.translate.get('Error.noValidEmail').subscribe(
      (value: string) => {
        this.strNoValidEmail = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.noValidEmail.", err);
      }
    );
    this.translate.get('Error.general').subscribe(
      (value: string) => {
        this.strError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.general.", err);
      }
    );
    this.translate.get('Error.noValidPassword').subscribe(
      (value: string) => {
        this.strNoValidPassword = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.noValidPassword.", err);
      }
    );
    this.translate.get('Error.usedEmail').subscribe(
      (value: string) => {
        this.strUsedEmail = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.usedEmail.", err);
      }
    );
    this.translate.get('Error.connection').subscribe(
      (value: string) => {
        this.strConnectionError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.connection.", err);
      }
    );
    this.translate.get('Error.confirmPassword').subscribe(
      (value: string) => {
        this.strConfirmPasswordError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.confirmPassword.", err);
      }
    );
    this.translate.get('Error.termsAndCondition').subscribe(
      (value: string) => {
        this.strTermsAndConditionError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.termsAndCondition.", err);
      }
    );
    this.translate.get('Success.register').subscribe(
      (value: string) => {
        this.strRegisterSuccess = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Success.register.", err);
      }
    );
  }



  /**
   * Username password and password repetitions get checked and registered with the server. If registry not successful
   * suiting error message gets displayed. if successful, logs in directly and goes either back to orderdetails or
   * homepage
   */
  public onRegisterClicked(username: string, password: string, password2: string): void {

    const user: User = {
      username: username,
      password: password
    };

    if (!this.validEmail(username)) {
      const toast: Toast = this.toastCtrl.create({
        message: this.strNoValidEmail,
        duration: 3000
      });
      toast.present();
    } else if (!this.passwordsIdentical(password, password2)) {
      const toast: Toast = this.toastCtrl.create({
        message: this.strConfirmPasswordError,
        duration: 3000
      });
      toast.present();

    } else if (!this.validPassword(password)) {
      const toast: Toast = this.toastCtrl.create({
        message: this.strNoValidPassword,
        duration: 3000
      });
      toast.present();
    } else if (!this.termsAndConditionsChecked) {
      const toast: Toast = this.toastCtrl.create({
        message: this.strTermsAndConditionError,
        duration: 3000
      });
      toast.present();
    } else {
      const loader: Loading = this.loading.prepareLoader();
      loader.present().then(() => {
        this.auth.register(username, password)
          .subscribe(
            resp => {
              const encodedCredentials: string = btoa(`${username}:${password}`);
              window.localStorage.setItem("username", username);
              window.localStorage.setItem(username, encodedCredentials);
              this.auth.userName = window.localStorage.getItem("username");
              this.auth.loggedIn = true;
              const toast: Toast = this.toastCtrl.create({
                message: this.strRegisterSuccess,
                duration: 3000
              });
              toast.present();
              this.push.pushSetup(user);

              //if coming from Orderdetailspage, go back there after registry
              if (this.goBack) {
                this.navCtrl.pop();
                loader.dismiss();

                //else go to Home
              } else {
                loader.dismiss();
                this.navCtrl.setRoot(TabsPage);
              }
            },
            err => {
              loader.dismiss();
              let alert: Alert;
              const body: string = err.toString();
              console.log(err.toString());
              switch (body) {
                case "1" :
                  alert = this.alertCtrl.create({
                    title: this.strError,
                    message: this.strNoValidEmail,
                    buttons: [{
                      text: 'Ok',
                      role: 'cancel'
                    }]
                  });
                  alert.present();
                  break;
                case "2" :
                  alert = this.alertCtrl.create({
                    title: this.strError,
                    message: this.strNoValidPassword,
                    buttons: [{
                      text: 'Ok',
                      role: 'cancel'
                    }]
                  });
                  break;
                case "3" :
                  alert = this.alertCtrl.create({
                    title: this.strError,
                    message: this.strUsedEmail,
                    buttons: [{
                      text: 'Ok',
                      role: 'cancel'
                    }]
                  });
                  break;

                default :
                  alert = this.alertCtrl.create({
                    title: this.strError,
                    message: this.strConnectionError,
                    buttons: [{
                      text: 'Ok',
                      role: 'cancel'
                    }]
                  });
              }
              alert.present();
            });
      });
    }
  }

  /**
   * Opens the terms and conditions site via inapp browser
   */
  public goToTermsAndConditions(): void {
    this.iab.create(`${SERVER_URL}/terms`, "location=yes");
  }

  /**
   * returns whether entered passwords are identical
   *
   * @param password
   * password first entered
   * @param password2
   * password repetition
   * @returns {boolean}
   * whether passwords are identical
   *
   */
  private passwordsIdentical(password: string, password2: string): boolean {
    return (password === password2);
  }

  /**
   * returns whether the email is valid
   *
   * @param username
   * email address
   * @returns {boolean}
   * wether the email is valid
   *
   */
  private validEmail(username: string){
    return (username.match(emailRegEX));
  }

  /**
   * returns whether the email is valid
   *
   * @param password
   * password
   * @returns {boolean}
   * wether the password is valid
   *
   */
  private validPassword(password: string){
    if(
      password.match(passwordLowercaseRegEx) &&
      password.match(passwordUppercaseRegEx) &&
      password.match(passwordDigitRegEx) &&
      password.match(passwordSpecialRegEx) &&
      password.match(passwordLengthRegEx))
    {return true;}
    return false;
  }


}
