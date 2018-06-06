import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {Push, PushObject, PushOptions, EventResponse} from "@ionic-native/push";
import {FCM} from "@ionic-native/fcm";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {SERVER_URL, APP_LANG, FCM_SENDER_ID} from "../app/app.module";
import {Alert, AlertController, Platform} from "ionic-angular";
import {Error} from "tslint/lib/error";
import {AuthService} from "./auth.service"
import {TranslateService} from "@ngx-translate/core";
import {User} from "../model/User";
import {DailyPushNotificationData} from "../model/DailyPushNotificationData";
import {Observable} from "rxjs/Observable";
import {Subscription} from "rxjs/Subscription";


/**
 * Initializing push and notification settings.
 * @author Sergej Bardin
 */
@Injectable()
export class PushService {

  private pushObject: PushObject;
  private strPushError: string;
  private strError: string;

  constructor(public push: Push,
              private alertCtrl: AlertController,
              private auth: AuthService,
              private http: HttpClient,
              private translate: TranslateService,
              private platform: Platform,
              private fcm: FCM) {
    this.translate.setDefaultLang(APP_LANG);
    this.translate.get('Error.pushReg').subscribe(
      (value: string) => {
        this.strPushError = value;
      },
      (err: Error) => {
        console.error("Error: translate.get did fail for key Error.pushReg.", err);
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
    this.platform.ready().then(
      () => {
        const pushOptions: PushOptions = {
          android: {
            senderID: FCM_SENDER_ID,
            vibrate: true
          },
          ios: {
            alert: 'false',
            badge: true,
            sound: 'true'
          }
        };
        this.pushObject = this.push.init(pushOptions);
        this.notificationSetup();
      },
      (err: Error) => {
        console.error("Platform did not become ready!", err);
        const alert: Alert = this.alertCtrl.create({
          title: this.strError,
          message: this.strPushError,
          buttons: [{
            text: 'Ok',
            role: 'cancel'
          }]
        });
        alert.present();
      });
  }

  /**
   * Registers a User to the PushNotification Service (Firebase)
   * @param {User} user = the User of the App
   * @param {boolean} getPushNotification = Does he want to get notificated
   */
  public manageUserPushNotfication(user: User, getPushNotification: boolean): void {
    const encodedCredentials: string = btoa(`${user.username}:${user.password}`);
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Basic ${encodedCredentials}`
    });

    // Aufruf der Rest API (Bei True mit Put, bei False mit Delete)
    if(getPushNotification){
      this.http.put(`${SERVER_URL}/api/pushNotifications`, {},{headers, responseType: 'text'}).subscribe(
        res => {
          console.log('push.service.ts - api/pushNotifications - put successfull');
          user.getNotification = true;
          const alert: Alert = this.alertCtrl.create({
            title: "Push Benachrichtigung wurden hinzugefügt!",
            message: "Ihre Anfrage auf regelmäßige Push Benachrichtigungen wurde erfolgreichverarbeitet! ",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        },
        err =>{
          console.log('push.service.ts - api/pushNotifications - put not successfull');
          console.error(JSON.stringify(err));
          user.getNotification = false;
          const alert: Alert = this.alertCtrl.create({
            title: "Error!",
            message: "Ihre Pushbenachrichtungen konnten nicht angelegt werden! ",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        }
      );
    }
    else{
      this.http.delete(`${SERVER_URL}/api/pushNotifications`,{headers, responseType: 'text'}).subscribe(
        res => {
          console.log('push.service.ts - api/pushNotifications - delete successfull');
          user.getNotification = false;
          const alert: Alert = this.alertCtrl.create({
            title: "Push Benachrichtigung entfernt!",
            message: "Ihr Push Benachrichtungen wurden erfolgreich entfernt! ",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        },
        err =>{
          console.log('push.service.ts - api/pushNotifications - delete not successfull');
          console.error(JSON.stringify(err));
          user.getNotification = true;
          const alert: Alert = this.alertCtrl.create({
            title: "Error!",
            message: "Ihre Pushbenachrichtungen konnten nicht ausgeschaltet werden! ",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        }
      );
    }
  }

  /**
   *  Setup of the display settings of the push notification
   */
  public notificationSetup(): void {
    //noinspection TsLint
    this.fcm.onNotification().subscribe(
      (data => {
        const alert: Alert = this.alertCtrl.create({
          title: "Bestellstatus geändert",
          message: "Der Status deiner Bestellung hat sich geändert. Du kannst ihn unter 'Meine Bestellungen' einsehen.",
          buttons: [{
            text: 'Ok',
            role: 'cancel'
          }, {
            text: 'Meine Bestellungen',
            handler: (): void => {
              this.openReservations();
            }
          }]
        });
        alert.present();
        if (data.wasTapped) {

        } else {
          console.warn("Not logged in or push permission NOT granted, reservation confirmation can not received!");
        }
        ;
      })
    );
  }

  /**
   * Register push token at backend, when user is logged in
   */
  public pushSetup(user: User): void {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',

    });
    this.platform.ready().then(
      () => {
        this.fcm.getToken().then(token => {

          this.http.put(`${SERVER_URL}/api/submitToken/${token}`, user, {headers})
            .retry(2)
            .subscribe(
              res => {
                console.warn("Device registered at firebase and backend");
                this.notificationSetup();
              },
              err => {
                console.error(err);
                const alert: Alert = this.alertCtrl.create({
                  title: this.strError,
                  message: this.strPushError,
                  buttons: [{
                    text: 'Ok',
                    role: 'cancel'
                  }]
                });
                alert.present();
              }
            );
        });
      }, err => {
        console.error("Platform did not become ready!", err);
        const alert: Alert = this.alertCtrl.create({
          title: this.strError,
          message: this.strPushError,
          buttons: [{
            text: 'Ok',
            role: 'cancel'
          }]
        });
        alert.present();
      });
  }

  /*
  Method to register the daily push notification via REST
  @param pushData the data that the notification needs
  @param user Username and Password of the user that is currently logged in
  @rest: /api/register_push
   */
  public registerPush(pushData: DailyPushNotificationData) {

    console.log('im dritten push');
    const encodedCredentials: string = btoa(`${pushData.user.username}:${pushData.user.password}`);
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Basic ${encodedCredentials}`
    });


    this.fcm.getToken().then(token => {

      console.log('im zweiten push');
      pushData.fcmToken = token;

      this.http.post(`${SERVER_URL}/api/register_push`, pushData, {headers}).retry(2).subscribe(
        res => {
          console.log('im vierten push');
          const alert: Alert = this.alertCtrl.create({
            title: "Push Benachrichtigung angelegt!",
            message: "Ihre Anfrage auf regelmäßige Push Benachrichtigungen wurde erfolgreichverarbeitet! ",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        }, err => {
          console.error(err);
          const alert: Alert = this.alertCtrl.create({
            title: this.strError,
            message: "Push Benachrichtigungen konnten nicht angelegt werden, es ist ein Fehler aufgetreten.",
            buttons: [{
              text: 'Ok',
              role: 'cancel'
            }]
          });
          alert.present();
        })
    })
  }

  private openReservations() {

  }


}
