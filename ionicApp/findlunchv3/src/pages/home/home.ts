import {Component, ElementRef, NgZone, OnInit, ViewChild} from "@angular/core";
import {Alert, AlertController, IonicPage, Loading, NavController, Platform, Popover, PopoverController} from "ionic-angular";
import {OffersPage} from "../offers/offers";
import {Restaurant} from "../../model/Restaurant";
import {FilterPopoverComponent} from "./FilterPopoverComponent";
import {FilterPopoverService} from "./FilterPopoverService";
import {LoadingService} from "../../shared/loading.service";
import {InformationService} from "../../shared/information.service";
import {TranslateService} from '@ngx-translate/core';

import {KitchenType} from "../../model/KitchenType";
import 'rxjs/add/operator/retry';
import 'rxjs/add/operator/timeout';
import {Network} from "@ionic-native/network";
import {Subscription} from "rxjs/Subscription";
import { Observable } from 'rxjs/Observable';
import {DailyPushNotificationData} from "../../model/DailyPushNotificationData";
import {PushService} from "../../shared/push.service";
import {ChooseLoginPage} from "../choose-login/choose-login";
import {AuthService} from "../../shared/auth.service";



// this is needed for google maps plugin v2
// noinspection TsLint
declare const plugin: any;
// noinspection TsLint
declare const cordova: any;

declare var google: any;

// constant used in this class - sets default map zoom level
const MAP_DEFAULT_ZOOM_LEVEL: number = 15;



/**
 * This Page displays the map and all restaurants on it.
 * It features filtering of the restaurants as well as manually setting your location
 *
 * Important Note: In order to get this to work on both iOS and Android devices, a beta-version of the cordova-plugin-googlemaps was used.
 * It could become unstable at some time, so please look at the package-lock.json if your version differs from the tested one.
 * Switch to a stable version of the v2-maps as soon as it is released (
 *
 * @author David Sautter
 */
@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit {

  // represents array of unfiltered restaurants
  public allRestaurants: Restaurant[];

  // reference to the rendered map HTML-element
  @ViewChild('map') public theMap: ElementRef;
  @ViewChild('searchbar', { read: ElementRef }) searchbar: ElementRef;
  addressElement: HTMLInputElement = null;

  // noinspection TsLint - no types for current plugin-googlemaps available
  map: any;
  // boolean for the Adressinputfield
  search: boolean = false;
  // noinspection TsLint - no types for current plugin-googlemaps available
  private mapMarkers: any[] = [];

  private location: {lat: number, lng: number};

  // initialize the strings to translate with the default values
  // noinspection TsLint
  private translatedStrs = {
    phone: "Tel.",
    address: "Adresse",
    distance: "Entfernung",
    kitchen: "Küche",
    isClosed: "Derzeit geschlossen",
    isOpen: "Jetzt geöffnet",
    retry: "retry",
    'Error.serviceNotAvailable': 'Error: Service not available!',
    'Error.general': 'General Error',
    close: "close"
  };

  constructor(private navCtrl: NavController,
              private auth: AuthService,
              private push: PushService,
              private popCtrl: PopoverController,
              private popService: FilterPopoverService,
              private platform: Platform,
              private zone: NgZone,
              private loading: LoadingService,
              private alertCtrl: AlertController,
              private translate: TranslateService,
              private network: Network,
              private info: InformationService) {


    // load the map as soon as the platform is ready
    this.platform.ready().then(

      () => {
        // translate needed strings for info-window
                this.loadMap();
                this.initAutocomplete();
      },
      (err: Error) => {
        console.error("Platform did not become ready!", err);
        alert("A fatal Error occured, the app will be closed!");
        platform.exitApp();
      });
  }

  /**
   * When initialized, do the translation of the needed strings.
   */

  public ngOnInit(): void {
      // translate needed strings for info-window
      Object.keys(this.translatedStrs).forEach((key: string) => {
        this.translate.get(key)
          .subscribe(
            (transStr: string) => this.translatedStrs[key] = transStr,
            (err: Error) => {
              // this should not be happening, because if translate does not
              // find strings, it uses the key. However something could be wrong...
              console.error(`Error: tranlate.get did fail for key ${key} with error: `, err);
            }
          );
      });
  }

  // noinspection JSUnusedGlobalSymbols - this is an ionic view lifecycle method so it is indeed used.
  /**
   * The Map must always be clickable when entering this page.
   */
  public ionViewDidEnter(): void {
    if (this.map) {
      this.map.setClickable(true);
      cordova.fireDocumentEvent('plugin_touch', {});      // gives native map focus
    }
  }

  /**
   * Construct and open the filter popup-dialog.
   * @param {Event} ev
   */
  public openFilterDialog(ev: Event): void {
    const pop: Popover = this.popCtrl.create(FilterPopoverComponent);
    pop.present({ev});

    pop.onDidDismiss(() => {
      this.setRestaurantMarkers(this.filterRestaurants(this.allRestaurants));
    });
  }


  /**
   * Applies the filters set in FilterPopoverService to the restaurants
   * @returns {Restaurant[]} the filtered restaurants to show
   */
  private filterRestaurants(allRestaurants: Restaurant[]): Restaurant[] {
    let newRestaurants: Restaurant[] = allRestaurants;
      console.log('kitchentypes holen');
    // filter kitchen types
    // this assumes, that kitchen-types are ALWAYS set on a restaurant
    if (this.popService.selectedKitchenTypes) {     // [] is also truthy, but if it is not loaded yet, this will skip
      newRestaurants = newRestaurants.filter((res: Restaurant) => {
        return res.kitchenTypes.some((resKitchenType: KitchenType) => {
          return this.popService.selectedKitchenTypes.some((selKitchenType: KitchenType) => {
            // at least one kitchentype must be matching the selected ones
            return resKitchenType.id === selKitchenType.id;
          });
        });
      });
    }

    // show only currently opened
    if (this.popService.showOnlyOpened) {
      newRestaurants = newRestaurants.filter((res: Restaurant) => res.currentlyOpen);
    }

    // show only favorites
    if (this.popService.showOnlyFavorites) {
      newRestaurants = newRestaurants.filter((res: Restaurant) => res.isFavorite);
    }

    return newRestaurants;
  }

  /**
   * Checks the internet connection and if it is ok,
   * initializes the Map and positions the current device on it.
   */
  loadMap(): void {
    // no connection or not good enough
    // check internet connection
    if (!(this.network.type.match(/^(3g|4g|wifi)$/))) {

      const alert: Alert = this.alertCtrl.create({
        title: this.translatedStrs['Error.general'],
        subTitle: this.translatedStrs['Error.serviceNotAvailable'],
        buttons: [
          {
            text: this.translatedStrs.close,
            role: 'cancel',
            handler: (): void => {
              this.platform.exitApp();
            }
          },
          {
            text: this.translatedStrs.retry,
            handler: (): void => {
              // watch network for a connection
              const connectSubscription: Subscription = this.network.onConnect().subscribe(() => {
                connectSubscription.unsubscribe();

                // need to wait a bit before determining network type again
                setTimeout(() => this.loadMap(), 3000);         // recursive call!
              });
            }
          }
        ]
      });

      alert.present();

    } else {

      var element = this.theMap.nativeElement;
      this.map = plugin.google.maps.Map.getMap(element, {
        controls: {
          compass: true,
          myLocation: true,
          myLocationButton: true,
          indoorPicker: true,
          disableDoubleClickZoom: false,
          zoom: true,
          mapToolbar: true   // currently Android only
        },
        gestures: {
          scroll: true,
          tilt: true,
          rotate: true,
          zoom: true
        }
      });

      // listen to MAP_READY event
      // You must wait for this event to fire before adding something to the map or modifying it in anyway
      this.map.one(plugin.google.maps.event.MAP_READY, () => {
          // noinspection TsLint - no types for current plugin-googlemaps available
          this.map.getMyLocation(
            null,
            (pos: any) => {
              // get restaurants around this location
              // currently, all restaurants are fetched, but I leave this code here (though it's slower this way),
              // because once you start really filtering restaurants based on the location, you'll need it here.
              console.log('Fetch Restaurants');
              this.location = pos.latLng;

              this.fetchRestaurants(pos.latLng);
              // move map to current location
              // noinspection TsLint - no types for current plugin-googlemaps available
              const camPos = {
                target: pos.latLng,
                zoom: MAP_DEFAULT_ZOOM_LEVEL
              };
              this.map.moveCamera(camPos);
            },
            (err: Error) => {
              console.error("Error getting location: ", err);
              this.search = true;
            });
        }
      );
    }
  }

  createAutocomplete(addressEl: HTMLInputElement): Observable<any> {
    const autocomplete = new google.maps.places.Autocomplete(addressEl);
    autocomplete.bindTo(this.map);
    return new Observable((sub: any) => {
      google.maps.event.addListener(autocomplete, 'place_changed', () => {
        const place = autocomplete.getPlace();
        if (!place.geometry) {
          sub.error({
            message: 'Autocomplete returned place with no geometry'
          });
        } else {
          console.log('Search Lat', place.geometry.location.lat());
          console.log('Search Lng', place.geometry.location.lng());
          sub.next(place.geometry.location);
         // sub.complete();
        }
      });
    });
  }

  initAutocomplete(): void {
    this.addressElement = this.searchbar.nativeElement.querySelector('.searchbar-input');
    this.createAutocomplete(this.addressElement).subscribe((location) => {
      console.log('Searchdata', location);

      let options = {
        center: location,
        zoom: 10
      };
      this.map.setOptions(options);
      this.fetchRestaurants(location);
    });
  }


  /**
   * Fetches the restaurants from the server using the provided coordinates. Initiates a loading
   * animation while fetching. It also triggers filtering the restaurants after they were fetched.
   * @param latLng location of type LatLng
   *
   *
   */
  private fetchRestaurants(latLng: { lat: number, lng: number }): void {

    // setup loading spinner
    const loader: Loading = this.loading.prepareLoader();
    loader.present().then(() => {

      // do not filter by radius, because there are just a few restaurants.
      // in the future it could filter by using the visible map-area.
      this.info.loadRestaurants(latLng, 99999).timeout(15000)
        .subscribe(
        res => {
          this.zone.run(() => {
            loader.dismiss();
            this.allRestaurants = res;
            console.log(this.allRestaurants.toString());
            this.setRestaurantMarkers(this.allRestaurants);
          });

        },
        err => {
          loader.dismiss();
          console.error("Error fetching restaurants", err);

          // Prompt user. Actions: Retry, Close App
          const alert: Alert = this.alertCtrl.create({
            title: this.translatedStrs['Error.general'],
            subTitle: this.translatedStrs['Error.serviceNotAvailable'],
            buttons: [
              {
                text: this.translatedStrs.close,
                role: 'cancel',
                handler: (): void => {
                  this.platform.exitApp();
                }
              },
              {
                text: this.translatedStrs.retry,
                handler: (): void => {
                  this.fetchRestaurants(latLng);      // recursive call, if the user selects retry
                }
              }
            ]

          });
          alert.present();
        }
      );
    });
  }


  /**
   *  Shows markers for the provided restaurants
   */
  private setRestaurantMarkers(restaurants: Restaurant[]): void {
    // remove old markers
    // noinspection TsLint - no types for current plugin-googlemaps available
    this.mapMarkers.forEach((marker: any) => {
      marker.remove();
    });

    // draw new markers
    restaurants.forEach((restaurant: Restaurant) => {
      try {
        // noinspection TsLint - no types for current plugin-googlemaps available
        this.map.addMarker({
          position: {lat: restaurant.locationLatitude, lng: restaurant.locationLongitude},
          icon: 'http://maps.google.com/mapfiles/kml/paddle/grn-stars.png'
        }, (marker: any) => {
          // add html info window
          // noinspection TsLint - no types for current plugin-googlemaps available
          const htmlInfoWindow: any = new plugin.google.maps.HtmlInfoWindow();

          const infoDiv: HTMLElement = document.createElement("div");

          // costruct the html-info-window content and fill with data
          // noinspection TsLint
          infoDiv.innerHTML = `<div style="display: inline-block">
<div style="font-size: large; font-weight: bold; margin-bottom: 5px">${restaurant.name}</div>
<div style="display: inline-block">${this.translatedStrs.address}: ${restaurant.street} ${restaurant.streetNumber}<br/>
${this.translatedStrs.phone}: ${restaurant.phone}<br/>
${this.translatedStrs.kitchen}: ${restaurant.kitchenTypes.map(kitType => kitType.name).join(', ')}<br/>
${this.translatedStrs.distance}: ${restaurant.distance}m<br/>
<span style="color: ${restaurant.currentlyOpen === true ? "green" : "red"}">${restaurant.currentlyOpen === true ? this.translatedStrs.isOpen : this.translatedStrs.isClosed}</span><div/>
</div>`;
          infoDiv.addEventListener("click", () => {
            this.zone.run(() => {
              this.navCtrl.push(OffersPage, {restaurant: restaurant}, {animate: false});
            });
          });

          // marker size and styling must be done manually
          infoDiv.style.maxWidth = "85%";
          infoDiv.style.display = "inline-block";
          infoDiv.style.padding = "0";

          // append this to the DOM for a short time to be able to calculate offsetHeight and -Width
          this.theMap.nativeElement.appendChild(infoDiv);
          infoDiv.style.height = `${infoDiv.offsetHeight + 1}px`;
          infoDiv.style.width = `${infoDiv.offsetWidth + 4}px`;
          this.theMap.nativeElement.removeChild(infoDiv);

          infoDiv.style.maxWidth = "none";
          infoDiv.style.margin = "6px";

          htmlInfoWindow.setContent(infoDiv);

          marker.on(plugin.google.maps.event.MARKER_CLICK, () => {
            htmlInfoWindow.open(marker);
          });

          // add marker to the map
          this.mapMarkers.push(marker);
        });
      } catch (err) {
        console.error("Error adding marker for restaurant", restaurant, err);
      }
    });
  }


  /**
   * Show the dialog for putting in an address, that will set the custom location
  */
  toggleSearch() {
    if (this.search) {
      this.search = false;
    } else {
      this.search = true;
    }
  }

  registerPush() {

    if (this.auth.getLoggedIn()) {
/*
      var pushData: DailyPushNotificationData = {
        latitude: this.location.lat,
        longitude: this.location.lng,
        radius: 99999,
        title: "",
        fcmToken: "",
        user: this.auth.getUser()
      };
      console.log('im ersten push');*/
      console.log('User getNotification: ' + this.auth.getUser().getNotification);
      this.push.manageUserPushNotfication(this.auth.getUser(), !this.auth.getUser().getNotification);
    }
     else {
      const alert: Alert = this.alertCtrl.create({
        title: "Fast geschafft!",
        subTitle: "Du bist fast soweit. Um Push-Benachrichtigungen anlegen zu können, musst du dich nur noch anmelden.",
        buttons: [
          {
            text: "Ok",
            role: 'cancel'
          },
          {
            text: "Jetzt anmelden",
            handler: (): void => {
              this.navCtrl.push(ChooseLoginPage);
            }
          }
        ]
      });
      alert.present();
    }

  }
/*
 * TODO: Aktivieren
 * Fürs erste ungenutzt, da Push-Benachrichtigungen an allen Tagen gesendet werden
 */
  public showCheckbox<alert>() {
    let alert = this.alertCtrl.create();
    alert.setTitle('An welchen Tagen würdest du gerne eine Benachrichtigung über die aktuellen Angebote haben?');

    alert.addInput({
      type: 'checkbox',
      label: 'Montag',
      value: 'value1',
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Dienstag',
      value: 'value2'
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Mittwoch',
      value: 'value3'
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Donnerstag',
      value: 'value4'
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Freitag',
      value: 'value5'
    });

    alert.addInput({
      type: 'checkbox',
      label: 'Samstag',
      value: 'value6'
    });
    alert.addInput({
      type: 'checkbox',
      label: 'Sonntag',
      value: 'value7'
    });

    return alert;
  }


}
