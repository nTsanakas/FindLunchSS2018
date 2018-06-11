import {Injectable} from "@angular/core";
import {SERVER_URL} from "../app/app.module";
import {Observable} from "rxjs/Observable";
import {Offer} from "../model/Offer";
import 'rxjs/add/operator/do';
import {HttpClient,HttpHeaders} from "@angular/common/http";


/**
 * Service for getting the Offers of a restaurant from the server and caching them.
 * @author David Sautter
 */
@Injectable()
export class OffersService {

    private cache: Map<number, Offer[]>;

    constructor(private http: HttpClient) {
        this.cache = new Map();
    }


    /**
     * Builds a string with the allergenic-ids and the additive-ids of a specific offer used to display
     * at the offer's product name.
     * It extracts the data from the provided offer-object and does not fetch from the server.
     *
     * @param offer offer-object containing allergenics and additives
     * @returns {string} A string containing all id's of the allergenics and additives, e.g. '1,4,7,a,g'
     */
    public static getALGsAndADDsOfOffer(offer: Offer): string {
        return offer.allergenic
            .concat(offer.additives)
            .map((a: {shortKey: string}) => a.shortKey)
            .join(",") || "";
    }


    /**
     * Retrieves all offers of a specific restaurant from the server. Caches requests and returns
     * them from the cache if user has already executed this method with the same restaurant-id.
     *
     * @param {number} restaurantId the restaurant to retrieve the offers from
     * @returns {Observable<Offer[]>} resolves to the retrieved offers

    */
  public loadOffers(restaurantId: number) : Observable<Offer[]> {

     if (this.cache.has(restaurantId)) {
       return Observable.of(this.cache.get(restaurantId)).take(1);
     }

    // get offers from server otherwise
    return this.http.get<Offer[]>(`${SERVER_URL}/api/restaurants/${restaurantId}/offers`)
  }

  public loadCurrentOffers(){
    const user: string = window.localStorage.getItem("username");
    const token: string = window.localStorage.getItem(user);
    let headers = new HttpHeaders({
      Authorization: `Basic ${token}`
    });
    return this.http.get<Offer[]>(`${SERVER_URL}/api/offers?longitude=11.5569&latitude=48.1543`,
      {headers});
    /*navigator.geolocation.getCurrentPosition(suc=> {
      console.log('Latitude: ' + suc.coords.latitude + 'Longtitude: ' + suc.coords.longitude);
      const user: string = window.localStorage.getItem("username");
        const token: string = window.localStorage.getItem(user);
        let headers = new HttpHeaders({
          Authorization: `Basic ${token}`
        });
      this.http.get<Map<number, Offer>>(`${SERVER_URL}/api/offers?longitude=${suc.coords.longitude}&latitude=${suc.coords.latitude}`, {headers})
        .subscribe(res => {
          console.log("/api/offers successfull");
          currentOffers = res;
          console.log(JSON.stringify(currentOffers));
        }, err => {
          console.error("/api/offers Error: " + JSON.stringify(err));
        });
    },
    err=> {
      console.error("GPS Position not available");
    });*/
  }

}
