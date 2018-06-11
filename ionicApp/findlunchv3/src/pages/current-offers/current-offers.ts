import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {Offer} from "../../model/Offer";
import {OffersService} from "../../shared/offers.service";
import {IonicPage, Loading, Content} from "ionic-angular";
import {LoadingService} from "../../shared/loading.service";
import {OfferProductDetailsPage} from "../offer-product-details/offer-product-details";
import {Restaurant} from "../../model/Restaurant";

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
  public currentOffersList: Offer[];

  constructor(private translate: TranslateService,
              private  offers: OffersService,
              private loading: LoadingService,
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
    loader.dismiss();
    this.offers.loadCurrentOffers()
    //.timeout(8000)
      .subscribe(
        resp => {
          this.currentOffersList = resp;
        },
        err => {
          loader.dismiss();
          console.error("Error on loading reservations.", err);

        },
        () => {

        }
      );
  }

  public onOfferClicked(event:Event, offer:any){
    var o:Offer={
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
    var restId = offer.restaurantId;
    let restaurant:Restaurant{

    }
    this.navCtrl.push(OfferProductDetailsPage, {o, restaurant: this.restaurant});

  }
}
