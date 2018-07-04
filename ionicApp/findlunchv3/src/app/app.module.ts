
/*
*
* Importieren der nötigen Angular-Plugins
*
**/
import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {IonicApp, IonicModule, IonicErrorHandler} from 'ionic-angular';
import {HttpClient, HttpClientModule} from "@angular/common/http";

import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
/*
*
* Importieren der Informationen für die App
*
**/
import { MyApp } from './app.component';
/*
*
* Importieren der Einzelnen Seiten der App
*
**/
import { AccountPage } from '../pages/account/account';
import { AccountDetailsPage} from "../pages/account-details/account-details";
import { HomePage } from '../pages/home/home';
import { TabsPage } from '../pages/tabs/tabs';
import { SettingsPage } from '../pages/settings/settings';
import {RegisterPage} from "../pages/register/register";
import {OffersPage} from "../pages/offers/offers";
import {BonusPage} from "../pages/bonus/bonus";
import {OfferProductDetailsPage} from "../pages/offer-product-details/offer-product-details";
import {RestaurantPage} from "../pages/restaurant/restaurant";
import {ReservationPage} from "../pages/reservation/reservation";
import {QRService} from "../pages/bonus/qr.service";
import {FilterPopoverComponent} from "../pages/home/FilterPopoverComponent";
import {FilterPopoverService} from "../pages/home/FilterPopoverService";
import {OffersService} from "../shared/offers.service";
import {ReservationsPage} from "../pages/reservations/reservations";
import {OrderDetailsPage} from "../pages/orderdetails/orderdetails";
import {CurrentOffersPage} from "../pages/current-offers/current-offers";
/*
*
* Importieren der Services für den Zugriff auf die Rest-Schnittstellen
*
**/

import {AuthService} from "../shared/auth.service";
import {CartService} from "../shared/cart.service";
import {LoadingService} from "../shared/loading.service";
import {PushService} from "../shared/push.service";
import {FavorizeService} from '../shared/favorize.service';
import {InformationService} from "../shared/information.service";
/*
*
* Importieren Ionic-Native Plugins
*
**/
import {InAppBrowser} from "@ionic-native/in-app-browser";
import {CommonModule, DatePipe} from "@angular/common";
import {BarcodeScanner} from "@ionic-native/barcode-scanner";
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import {NativeGeocoder} from "@ionic-native/native-geocoder";
import {Push} from "@ionic-native/push";
import {Network} from "@ionic-native/network";
import {ChooseLoginPage} from "../pages/choose-login/choose-login";
import {FCM} from "@ionic-native/fcm";


export const SERVER_URL: string = "http://192.168.43.113:8080";
export const APP_LANG: string = "de";
export const FCM_SENDER_ID: string = '101435960151';

@NgModule({
  declarations: [
    MyApp,
    TabsPage,
    OffersPage,
    BonusPage,
    OrderDetailsPage,
    FilterPopoverComponent,
    OfferProductDetailsPage, 
    RestaurantPage,
    ReservationsPage,
    ReservationPage,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]}
    }),
    HttpClientModule,
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    ChooseLoginPage,
    MyApp,
    AccountPage,
    AccountDetailsPage,
    RegisterPage,
    HomePage,
    TabsPage,
    SettingsPage,
    OffersPage,
    BonusPage,
    OrderDetailsPage,
    FilterPopoverComponent,
    OfferProductDetailsPage,
    ReservationPage,
    RestaurantPage,
    ReservationsPage,
    CurrentOffersPage
  ],
  providers: [
    FCM,
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    FilterPopoverService,
    NativeGeocoder,
    OffersService,
    BarcodeScanner,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    QRService,
    CartService,
    Push,
    InformationService,
    AuthService,
    InAppBrowser,
    PushService,
    LoadingService,
    DatePipe,
    Network,
    FavorizeService,
  ]
})
export class AppModule {}

/**
 * Function prepares the loader for the translation service
 * from './assets/i18n/*.json'.
 * @param http
 * @returns {TranslateLoader}
 */
export function createTranslateLoader(http: HttpClient): TranslateHttpLoader{
  return new TranslateHttpLoader(http, './assets/i18n/', '.json')
}
