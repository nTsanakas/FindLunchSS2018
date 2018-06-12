import {Injectable} from '@angular/core';
import {HttpHeaders, HttpClient, HttpResponse} from "@angular/common/http";
import {SERVER_URL} from "../app/app.module";

import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";


/**
 *Service that handles the roundtrips for favorizing and defavorizing a restaurant
 *
 */
@Injectable()

export class FavorizeService{

    constructor(public http: HttpClient,
                private auth: AuthService) {
    }

    /**
     * toggles the "favorite" status for a user's restaurant
     * @param isFavorite boolean that provides the info whether the restaurant is a favorite at the moment
     * @param restaurantID ID of the according restraunt to (de-)favorize
     * @returns {Observable<Response>} Observable that communicates whether round trip was successful
     */
    public toggleFavorize(isFavorite: boolean, restaurantID: number): Observable<HttpResponse<Object>> {
      let headers: HttpHeaders;
      if (isFavorite) {
        headers = this.auth.prepareHttpOptions();
        return this.http.delete<Object>(`${SERVER_URL}/api/unregister_favorite/${restaurantID}`, {
          observe: 'response',
          headers
        });
      } else {
        headers = this.auth.prepareHttpOptions();
        return this.http.put<Object>(`${SERVER_URL}/api/register_favorite/${restaurantID}`, "", {
          observe: 'response',
          headers
        });
      }
    }
}
