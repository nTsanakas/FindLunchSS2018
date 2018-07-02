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
            console.log("Current Offers: " + this.currentOffersList);
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

  public onOfferClicked(event:Event, offer1:any){

    let offer:Offer = offer1;

    let restaurant:Restaurant = offer1.restaurant;
    this.navCtrl.push(OfferProductDetailsPage, {offer, restaurant: restaurant});

  }
}
