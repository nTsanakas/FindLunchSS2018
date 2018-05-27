import {Component, OnInit} from "@angular/core";
import { Response} from "@angular/http";
import {SERVER_URL} from "../../app/app.module";
import {QRService} from "./qr.service";
import {TranslateService} from '@ngx-translate/core';
import {LoadingService} from "../../shared/loading.service";
import {AuthService} from "../../shared/auth.service";
import {InformationService} from "../../shared/information.service";
import {Loading, Alert, AlertController, NavController} from "ionic-angular";
import {Error} from "tslint/lib/error";
import {HomePage} from "../home/home";
import {HttpClient, HttpHeaders} from "@angular/common/http";


/**
 * This pages loads and shows the points of an user per restaurant.
 * Also shows the barcode scanner (qr scanner) function.
 * @author Sergej Bardin
 */
@Component({
    templateUrl: 'bonus.html'
})
export class BonusPage implements OnInit {
    //noinspection TsLint
    public points: any[];
    private strLoadPointsError: string;
    private strGeneralError: string;
    private strCancel: string;
    private strRetry: string;
    constructor(private http: HttpClient,
                private qr: QRService,
                private auth: AuthService,
                private alertCtrl: AlertController,
                private loading: LoadingService,
                private translate: TranslateService,
                private navCtrl: NavController,
                private info: InformationService) {
    }
    public ngOnInit() : void {
        this.translate.get('Error.points').subscribe(
            (value: string) => {
                this.strLoadPointsError = value;
                },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.points.", err);
            });
        this.translate.get('Error.general').subscribe(
            (value: string) => {
                this.strGeneralError = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.general.", err);
            });
        this.translate.get('retry').subscribe(
            (value: string) => {
                this.strRetry = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key retry.", err);
            });
        this.translate.get('cancel').subscribe(
            (value: string) => {
                this.strCancel = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key cancel.", err);
            });
        this.getPoints();
    }
    /**
     * Opens the barcode scanner(camera) of the device via service
     */
    private onQRClicked (event: Event) : void {
        this.qr.onQRClicked(event)
            .then(() => {
                this.getPoints();
            });
    }

    /**
     * Loads available points of an authorized user per restaurant
     *
     */
    private getPoints () : void {

      const loader: Loading = this.loading.prepareLoader();
      loader.present();
      this.info.loadPoints().subscribe(

        //prepare and start loading spinner
        resp => {
          this.points = resp;
          loader.dismiss();
        },
        err => {
          console.error("Getting user points error.", err);
          loader.dismiss();
          //noinspection TsLint
          const alert: Alert = this.alertCtrl.create({
            title: this.strGeneralError,
            subTitle: this.strLoadPointsError,
            buttons: [
              {
                text: 'Ok',
                role: 'cancel',
                handler: () => {
                  this.navCtrl.setRoot(HomePage);
                }
              },
              {
                text: this.strRetry,
                handler: () => {
                  this.getPoints();
                }
              }
            ]

          });
          alert.present();
        }
      );
    }
}
