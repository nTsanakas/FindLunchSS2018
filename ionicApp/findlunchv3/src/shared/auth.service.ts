import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {SERVER_URL} from "../app/app.module";
import {User} from "../model/User";
import {Observable} from "rxjs/Observable";

/**
 * Service that handles everything about login, verification and registration of users of the App.
 *
 * @author Skanny Morandi - refactored by Sergej Bardin
 */
@Injectable()
export class AuthService {
    public loggedIn: boolean;
    public userName: string;

    constructor(private http: HttpClient) {
    }

    /**
     * Checks whether the username and password provided are present in the backend database.
     * if yes, this method writes the credentials into the local storage of the device,
     * to make it available even after closing the app and therefore the user doesn't have to login
     * on every startup
     *
     * @param username
     * username, the user's e-mail adress
     * @param password
     * password of the user
     * result returned to the method that called the login-functionality
     */
    public login(username: string, password: string): Observable<boolean> {

      const encodedCredentials: string = btoa(`${username}:${password}`);
      const headers: HttpHeaders = new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Basic ${encodedCredentials}`
      });

      return this.http.get<boolean>(`${SERVER_URL}/api/login_user`,
        {headers});
    }

    /*
     * Registers user and if successful also directly logs the user in.
     * @param userName
     *  chosen username
     * @param password
     *  chosen password
     *  result whether registration was successful returned to the calling method
     */
    public register  (username: string, password: string) : Observable<boolean> {
      const user: User = {
        username: username,
        password: password
      };

      const headers: HttpHeaders = new HttpHeaders({
        'Content-Type': 'application/json'
      });

     return this.http.post<boolean>(`${SERVER_URL}/api/register_user`, user, {headers});

    }

    /**
     * verifies whether the user stored in the local storage is still existent in backend database.
     * If no more existent, logs the user out
     *
     */
    public verifyUser() : void {
        //if there is a username stored at all in the local storage...
        if (window.localStorage.getItem("username") !== null) {
            const headers: HttpHeaders = this.prepareHttpOptions();
            this.http.get(`${SERVER_URL}/api/login_user`, {headers}).subscribe(
                res => {
                    //if verification successful..
                    this.loggedIn = true;
                    this.userName = window.localStorage.getItem("username");
            },
                err => {
                    console.log("User konnte nicht verifiziert werden (Fehler in: verifyUser())");
                    this.logout();
            });
        }
    }

    /**
     * Returns whether the current user is verified, aka his "logged in"-status
     * @returns {boolean}
     * logged-in status of user
     */
    public getLoggedIn(): boolean {
        return this.loggedIn;
    }

    /**
     * Gets username of the current User
     * @returns {string}
     *  username of current user
     */
    public getUserName(): string {
        return this.userName;
    }

    /**
     * logs the current user out. Clears his username and token from the local storage.
     */
    public logout(): void {
        const currentUser: string = window.localStorage.getItem("username");

        //delete key-value pair stored under the key named after the most recently logged in user
        window.localStorage.removeItem(currentUser);
        // delete latest logged in user stored under key "username"
        window.localStorage.removeItem("username");
        this.loggedIn = false;
        this.userName = "";
    }

    /**
     * prepares the options object for http-requests. If user is logged in, authentication header
     * is sent along, otherwise RequestHeaders get sent along "empty".
     *
     * Request method that represents the http-method used
     * RequestMethod.Get .Put .Delete .Post etc.
     */
    public prepareHttpOptions(): HttpHeaders {

        let headers: HttpHeaders;

        if (this.getLoggedIn()) {
            const user: string = window.localStorage.getItem("username");
            const token: string = window.localStorage.getItem(user);
            headers = new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Basic ${token}`
            });
        }
        return headers;
    }
}
