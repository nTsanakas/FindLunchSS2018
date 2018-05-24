
import {User} from "./User";

export interface DailyPushNotificationData {
  title: string,
  fcmToken: string,
  latitude: number,
  longitude: number,
  radius: number,
  user: User,
}
