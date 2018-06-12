import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {Offer} from "../../model/Offer";
import {OffersService} from "../../shared/offers.service";
import {IonicPage, Loading, Content, NavController} from "ionic-angular";
import {LoadingService} from "../../shared/loading.service";
import {OfferProductDetailsPage} from "../offer-product-details/offer-product-details";
import {Restaurant} from "../../model/Restaurant";
import {copySourcemaps} from "@ionic/app-scripts/dist/util/source-maps";

/**
 * This pages loads and shows all current Offers.
 */
@IonicPage()
@Component({
  selector: 'current-offers',
  templateUrl: 'current-offers.html'
})
export class CurrentOffersPage implements OnInit {

  @ViewChild(Content) content: Content;
  public currentOffersList: any[];

  constructor(private translate: TranslateService,
              private  offers: OffersService,
              private loading: LoadingService,
              public navCtrl: NavController
  ) {
    this.currentOffersList = [];
  }

  /**
   *
   */
  public ngOnInit(): void {
    console.log("Current Offers Loading");
    this.loadCurrentOffers();
  }


  private loadCurrentOffers() {
    const loader: Loading = this.loading.prepareLoader();
    loader.present();
    this.offers.loadCurrentOffers()
      .subscribe(
        resp => {
            this.currentOffersList = resp;
            console.log(this.currentOffersList);
            loader.dismiss();
        },
        err => {
          loader.dismiss();
          console.error("Error on loading reservations.", err);

        },
        () => {

        }
      );
    loader.dismiss();
  }

  public onOfferClicked(event:Event, offer:any){
    console.log("Click: "+JSON.stringify(offer));
    let o:Offer={
      id: offer.id,
      description: offer.description,
      preparationTime: offer.preparationTime,
      price: offer.price,
      title: offer.title,
      defaultPhoto: offer.defaultPhoto,
      neededPoints: offer.neededPoints,
      amount: 0,
      additives: offer.additives,
      allergenic: offer.allergenic,
      sold_out: offer.sold_out,
      courseType: {id:0, name: ''}
    };
    console.log("o ok");
    let restaurant:Restaurant = offer.restaurant;
    console.log("res ok");
    /*let restaurant:Restaurant={
      actualPoints: offer.actualPoints,
      city: offer.city,
      country: offer.country,
      currentlyOpen: offer.currentlyOpen,
      distance: offer.distance,
      email: offer.email,
      id: offer.id,
      isFavorite: offer.isFavorite,
      kitchenTypes: offer.kitchenTypes,
      locationLatitude: offer.locationLatitude,
      locationLongitude: offer.locationLongitude,
      name: offer.name,
      phone: offer.phone,
      restaurantType: offer.restaurantType,
      street: offer.street,
      streetNumber: offer.streetNumber,
      timeSchedules: offer.timeSchedules,
      url: offer.url,
      zip: offer.zip
    };*/
    this.navCtrl.push(OfferProductDetailsPage, {offer: o, restaurant: restaurant});

  }
}
