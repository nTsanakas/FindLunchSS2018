import {Offer} from "./Offer";
import {Restaurant} from "./Restaurant";

export interface Reservation {
  id: number;
  donation: number;
  totalPrice: number;
  usedPoints: boolean;
  points: number;
  pointsCollected: boolean;
  reservationNumber: number;
  items: Offer[];
  reservation_offers?: Offer[];
  restaurant: Restaurant;
  bill: number;
  reservationStatus:
    {
      id: number, status: string, key: number
    };
  collectTime: number;
}
