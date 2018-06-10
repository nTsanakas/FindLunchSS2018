import {Component, OnInit} from "@angular/core";
import {Alert, AlertController, Loading, NavController, NavParams, Platform} from "ionic-angular";
import {OffersService} from "../../shared/offers.service";
import {OfferProductDetailsPage} from "../offer-product-details/offer-product-details";
import {OrderDetailsPage} from "../orderdetails/orderdetails";
import {Restaurant} from "../../model/Restaurant";
import {RestaurantPage} from "../restaurant/restaurant";
import {Observable} from "rxjs/Observable";

import {CartService} from "../../shared/cart.service";
import {AuthService} from "../../shared/auth.service";
import {InformationService} from "../../shared/information.service";
import {TranslateService} from "@ngx-translate/core";
import {LoadingService} from "../../shared/loading.service";
import {Offer} from "../../model/Offer";
import {Error} from "tslint/lib/error";
import 'rxjs/add/operator/catch';
import {FavorizeService} from "../../shared/favorize.service";

/**
 * Page for showing the offers of a specific restaurant in a list.
 * If the user clicks on an offer, she will get to the detail view of this offer.
 * @author David Sautter, Skanny Morandi
 */
@Component({
    templateUrl: 'offers.html'
})
export class OffersPage implements OnInit {
    public restaurant: Restaurant;
    public offers: Offer[];

    // need this to be able to call a static function from the template
    public getALGsAndADDsOfOffer: Function = OffersService.getALGsAndADDsOfOffer;
    public allergenics$: Observable<string>;
    public additives$: Observable<string>;

    public categories: string[];

    private shownGroup: string = null;
    private strErrorFavorize: string;
    private strErrorDeFavorize: string;
    private strOpeningError: string;
    private strError: string;

    constructor(navParams: NavParams,
                public offerService: OffersService,
                private cartService: CartService,
                private navCtrl: NavController,
                public auth: AuthService,
                private platform: Platform,
                private loading: LoadingService,
                private alertCtrl: AlertController,
                private fav: FavorizeService,
                private translate: TranslateService,
                private info: InformationService) {

        this.restaurant = navParams.get("restaurant");

        // disable back-animation, because it causes problems with displaying the map on iOS
        platform.ready().then(() => {
            platform.registerBackButtonAction(() => {
                this.navCtrl.pop({animate: false});
            });
        });
    }

    public ngOnInit(): void {
        this.translate.get('Error.favorize').subscribe(
            (res: string) => {
                this.strErrorFavorize = res;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.favorize.", err);
            });
        this.translate.get('Error.deFavorize').subscribe(
            (res: string) => {
                this.strErrorDeFavorize = res;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.deFavorize.", err);
            });
        this.translate.get('Error.openingProblem').subscribe(
            (str: string) => {
                this.strOpeningError = str;
            },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.openingProblem.", err);
            });
        this.translate.get('Error.general').subscribe(
            (value: string) => {
                this.strError = value;
                },
            (err: Error) => {
                console.error("Error: translate.get did fail for key Error.general.", err);
            }
        );

      this.offerService.loadOffers(this.restaurant.id).subscribe(resp => {
          this.offers = resp;
          this.categories = Object.keys(this.offers);
          this.shownGroup = this.categories[0] || null;
        },
        err => {
          console.error("Error retrieving offers of restaurant: ", this.restaurant, err);
          alert(this.strOpeningError);
          this.navCtrl.pop(); // go back so that the user can select another restaurant
        }
      );
        // get the allergenics, and additives from the server
        this.info.loadAllergenics()
            .subscribe(resp => {this.allergenics$ = resp},
              err => {
                console.error("Error retrieving allergenics: ", err);
                return Observable.of("");
              }
              );
        this.info.loadAdditives()
            .subscribe(resp => {this.additives$ = resp},
              err => {
                console.error("Error retrieving allergenics: ", err);
                return Observable.of("");
              }
              );
    }

    /**
     * Navigates to the Product details page of the clicked menu item.
     * @param event
     *  the click
     * @param offer
     *  the clicked offer
     * @author Skanny Morandi
     */
    public onOfferClicked(event: Event, offer: Offer): void {
        this.navCtrl.push(OfferProductDetailsPage, {offer, restaurant: this.restaurant});
    }

    /**
     * Navigate to the RestaurantPage of the restaurant whos offers are shown on click
     * current restaurant Object is sent along
     * @param event
     *  the click
     * @author Skanny Morandi
     */
    public onRestaurantClicked(event: Event): void {
        this.navCtrl.push(RestaurantPage, {restaurant: this.restaurant});
    }

    /**
     * Toggles the isFavorite status of the restaurant and also sends this to the server.
     * Shows a loading animation while request is running
     * @author Skanny Morandi
     */
    public toggleIsFavorite(): void {
        // prepare loader
        const loader: Loading = this.loading.prepareLoader();
        loader.present();

        // unset as favorite if already favorite
        if (this.restaurant.isFavorite) {
            this.fav.toggleFavorize(this.restaurant.isFavorite, this.restaurant.id)
                .timeout(8000)
                .subscribe(
                    resp => {
                        if (resp) {
                            this.restaurant.isFavorite = false;
                            loader.dismiss();
                        } else {
                            loader.dismiss();
                            const alert: Alert = this.alertCtrl.create({
                                title: this.strError,
                                message: this.strErrorDeFavorize,
                                buttons: [{
                                    text: 'Ok',
                                    role: 'cancel'
                                }]
                            });
                            alert.present();
                        }
                    },
                    err => {
                        loader.dismiss();
                        console.error("Defavorize restaurant failed.", err);
                        const alert: Alert = this.alertCtrl.create({
                            title: this.strError,
                            message: this.strErrorDeFavorize,
                            buttons: [{
                                text: 'Ok',
                                role: 'cancel'
                            }]
                        });
                        alert.present();
                    });
        // if not yet set as favorite, set it
        } else {
            this.fav.toggleFavorize(this.restaurant.isFavorite, this.restaurant.id)
                .timeout(8000)
                .subscribe(
                resp => {
                  if (resp){
                    this.restaurant.isFavorite = true;
                    loader.dismiss();
                  } else {
                    loader.dismiss();
                    const alert: Alert = this.alertCtrl.create({
                      title: this.strError,
                      message: this.strErrorDeFavorize,
                      buttons: [{
                        text: 'Ok',
                        role: 'cancel'
                      }]
                    });
                    alert.present();
                  }
                }, err => {
                  loader.dismiss();
                  console.error("Defavorize restaurant failed.", err);
                  const alert: Alert = this.alertCtrl.create({
                    title: this.strError,
                    message: this.strErrorDeFavorize,
                    buttons: [{
                      text: 'Ok',
                      role: 'cancel'
                    }]
                  });
                  alert.present();
                });

        }
    }

    /**
     * Gets the number of items in the cart for the cartsymbol badge
     * @returns {number} number of items in current cart for chosen restaurant
     */
    public getCartItemCount(): number {
        return this.cartService.getCartItemCount(this.restaurant.id);
    }

    /**
     * Toggles a food category on click and shows the food items in it
     * @param group {string}
     *  according food category
     */
    private toggleGroup(group: string): void {
        if (this.isGroupShown(group)) {
            this.shownGroup = null;
        } else {
            this.shownGroup = group;
        }
    }

    /**
     * Returns whether the items of a food category are to be shown or hidden
     * by being collapsed
     * @param group {string}
     *  the according food category
     * @returns {boolean}
     *  boolean whether items are to be shown or not.
     */
    private isGroupShown(group: string): boolean {
        return this.shownGroup === group;
    }

    /**
     * sends user to the OrderDetails page, along with the restaurant object
     */
    private goToCheckout(): void {
        this.navCtrl.push(OrderDetailsPage, {
            restaurant: this.restaurant
        });
    }
}

