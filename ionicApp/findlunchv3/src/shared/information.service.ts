
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Restaurant} from "../model/Restaurant";
import {Observable} from "rxjs/Observable";
import {SERVER_URL} from "../app/app.module";
import {Reservation} from "../model/Reservation";
import {KitchenType} from "../model/KitchenType";
import {AuthService} from "./auth.service";
import {Injectable} from "@angular/core";

/**
 * Service to get certain Information about Reservations, Offers ... via Rest
 *
 */

@Injectable()
export class InformationService {

  public restaurant: Restaurant;

  constructor(private http: HttpClient, private auth: AuthService) {

  }


  /*
   * Method to get the Reservations of the User
   *
   * @return: Observable<HttpResponse<Offer[]>>
   */
  public loadCustomerReservations(): Observable<Reservation[]>{
    const headers: HttpHeaders = this.auth.prepareHttpOptions();
    return this.http.get<Reservation[]>(`${SERVER_URL}/api/getCustomerReservations`, {headers});
  }

  /*
  * Method to get the Bonus Points of the User
  *
  * @return: Observable<HttpResponse<any[]>>
  */
  public loadPoints () : Observable<any[]> {
    //prepare http-options
    const headers: HttpHeaders = this.auth.prepareHttpOptions();
    return this.http.get<any[]>(`${SERVER_URL}/api/get_points`, {headers})
  }

  /*
  * Method to get the Kitchentypes
  *
  * @return: Observable<HttpResponse<KitchenType[]>>
  */
  public loadKitchenTypes(): Observable<KitchenType[]>{
    return this.http.get<KitchenType[]>(`${SERVER_URL}/api/kitchen_types`);
  }

  public loadAllergenics():  Observable<Observable<string>>{

    return this.http.get<Observable<string>>(`${SERVER_URL}/api/all_allergenic`);
  }

  public loadAdditives(): Observable<Observable<string>>{

    return this.http.get<Observable<string>>(`${SERVER_URL}/api/all_additives`);

  }

  public loadRestaurants(latLng: { lat: number, lng: number }, rad: number): Observable<Restaurant[]>{

    const headers: HttpHeaders = this.auth.prepareHttpOptions();

    return this.http.get<Restaurant[]>(`${SERVER_URL}/api/restaurants?latitude=${latLng.lat}&longitude=${latLng.lng}&radius=${rad}`,
      {headers})
  }



}
