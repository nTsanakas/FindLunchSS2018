import {Component, OnInit} from "@angular/core";
import {Alert, AlertController, Loading, NavController} from "ionic-angular";
import { Response} from "@angular/http";
import {SERVER_URL} from "../../app/app.module";
import {ReservationPage} from "../reservation/reservation";
import {Reservation} from "../../model/Reservation";
import {TranslateService} from "@ngx-translate/core";
import {AuthService} from "../../shared/auth.service";
import {LoadingService} from "../../shared/loading.service";
import {HomePage} from "../home/home";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {InformationService} from "../../shared/information.service";

/**
 * This pages loads and shows all reservation of an user.
 * @author Sergej Bardin & Skanny Morandi
 */
@Component({
    templateUrl: 'reservations.html'
})
export class ReservationsPage implements OnInit {
    public reservations: Reservation[];
    public usedRestaurants: String[];
    private strReservationError: string;
    private strGeneralError: string;
    private strRetry: string;
    private strCancel: string;

    constructor(public navCtrl: NavController,
                private http: HttpClient,
                private auth: AuthService,
                private loading: LoadingService,
                private alertCtrl: AlertController,
                private translate: TranslateService,
                private info: InformationService) {
        this.usedRestaurants = [];
    }
    /**
     * Loads the reservation(s) of a user.
     */
    public ngOnInit(): void {
        this.translate.get('Error.reservation').subscribe(
            (value: string) => {
                this.strReservationError = value;
                },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.reservation.", err);
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
        this.loadReservations();
    }

    /**
     * Saving the restaurants from reserveration to array
     */
    public collectUsedRestaurants(): void {
        for (const reservation of this.reservations) {
            if (this.usedRestaurants.indexOf(reservation.restaurant.name) === -1) {
                this.usedRestaurants.push(reservation.restaurant.name);
            }
        }
    }

    /**
     * Opens a reservation detail view on click.
     * @param event
     * @param reservation
     */
    public onReservationClicked(event: Event, reservation: String): void {
        this.navCtrl.push(ReservationPage, {reservation: reservation});
    }

    /**
     * Sorts several reserverations by collect time
     * @param reservations
     *
     * TODO: res.Json!!!
     */
    public sortByCollectTime(reservations: Reservation[]): void {
        reservations.sort((a: Reservation, b: Reservation) => {
            if (a.collectTime > b.collectTime) { return -1; }
            if (b.collectTime > a.collectTime) { return 1; }
            return 0;
        });
    }

    private loadReservations(): void {
        //prepare a loading spinner
        const loader: Loading = this.loading.prepareLoader();
        loader.present();

        this.info.loadCustomerReservations()
            //.timeout(8000)
            .subscribe(
              resp =>{
                    this.reservations = resp;
                    if (this.reservations.length > 0) {
                        this.collectUsedRestaurants();
                        this.sortByCollectTime(this.reservations);
                    }
                    loader.dismiss();
                },
                err => {
                    loader.dismiss();
                    console.error("Error on loading reservations.", err);
                    //noinspection TsLint
                    const alert: Alert = this.alertCtrl.create({
                        title: this.strGeneralError,
                        subTitle: this.strReservationError,
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
                                    this.loadReservations();
                                }
                            }
                        ]
                    });
                    alert.present();
                }
            );
    }
}
