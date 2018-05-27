import {Injectable} from '@angular/core';
import {Toast, ToastController} from "ionic-angular";
import {SERVER_URL} from "../../app/app.module";
import {BarcodeScanner, BarcodeScanResult} from '@ionic-native/barcode-scanner';
import {TranslateService} from "@ngx-translate/core";
import {AuthService} from "../../shared/auth.service";
import {Error} from "tslint/lib/error";
import {HttpClient, HttpHeaders} from "@angular/common/http";
/**
 * Handles the barcode scanner function of the device.
 * With the barcode scanner it is possible to scan qr codes
 * and send it to the backend to confirm a reservation.
 * @author Sergej Bardin
 */
@Injectable()
export class QRService {
    private strConfirmOrderSuccess: string;
    private strRestaurantNotFound: string;
    private strOfferNotFound: string;
    private strQRError: string;

    constructor(private barcodeScanner: BarcodeScanner,
                private toastCtrl: ToastController,
                private http: HttpClient,
                private auth: AuthService,
                private translate: TranslateService) {
        this.init();
    }

    public init(): void {
        this.translate.get('Success.confirmOrder').subscribe(
            (value: string) => {
                this.strConfirmOrderSuccess = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Success.confirmOrder.", err);
            });
        this.translate.get('Error.confirmOrderRestaurant').subscribe(
            (value: string) => {
                this.strRestaurantNotFound = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.confirmOrderRestaurant.", err);
            });
        this.translate.get('Error.confirmOrderOffer').subscribe(
            (value: string) => {
                this.strOfferNotFound = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.confirmOrderOffer.", err);
            });
        this.translate.get('Error.qr').subscribe(
            (value: string) => {
                this.strQRError = value;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.qr.", err);
            });
    }

    /**
     * Handles the barcode scanner function of the device.
     * Sends a request with a qr code to the backend.
     * While request is underway,
     * There will displayed a message if:
     * - succesfully confirmed an reservation
     * - there is no restaurant(qr code unknown)
     * - there is no offer
     * - the request to the backend fails
     * - the devices barcode scanner got a problem.
     *
     * @param event
     * @return promise of barcode result
     */
    public onQRClicked(event: Event): Promise<void> {
        return this.barcodeScanner.scan()
            .then(
                (barcodeData: BarcodeScanResult) => {
                    if (!barcodeData.cancelled) {

                        //preparing HttpHeaders
                        const headers: HttpHeaders = this.auth.prepareHttpOptions();

                        this.http.get<number>(`${SERVER_URL}/api/confirm_reservation/${barcodeData.text}`, {headers})
                            .retry(2)
                            .subscribe(
                                res => {
                                    let msg: string;
                                    switch (res) {
                                        case 0:
                                            msg = this.strConfirmOrderSuccess;
                                            break;
                                        case 3:
                                            msg = this.strRestaurantNotFound;
                                            break;
                                        case 4:
                                            msg = this.strOfferNotFound;
                                            break;
                                        default:
                                            msg = this.strQRError;
                                            break;
                                    }
                                    const toast: Toast = this.toastCtrl.create({
                                        message: msg,
                                        duration: 3000
                                    });
                                    toast.present();
                                },
                                (err: Error) => {
                                    console.error("QR Scan: Server response error!", err);
                                    const toast: Toast = this.toastCtrl.create({
                                        message: this.strQRError,
                                        duration: 3000
                                    });
                                    toast.present();
                                }
                            );
                    } else {
                        console.warn("QR-Scan abort!");
                    }
                },
                err => {
                    console.error("QR-Scan Error!", err);
                    const toast: Toast = this.toastCtrl.create({
                        message: this.strQRError,
                        duration: 3000
                    });
                    toast.present();
                });
    }
}
