package edu.hm.cs.projektstudium.findlunch.webapp.push;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.hm.cs.projektstudium.findlunch.webapp.model.DailyPushNotificationData;
import edu.hm.cs.projektstudium.findlunch.webapp.model.PushToken;
import edu.hm.cs.projektstudium.findlunch.webapp.model.Reservation;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.PushNotificationRepository;
/**
 * 
 * Class PushNotificationManager.
 * 
 * Called from scheduled class "PushNotificationScheduledTask" for the daily notifications.
 * 
 * Handles live-operation Amazon SNS and Google FCM push-notification messaging.
 * Requires provider credentials for live-operation:
 * #Credentials configuration:
 * **Configure LiveOpCredentials.conf, representing Amazon/Google identification
 * 
 * Base class for sending Amazon or Google push message.
 * 
 * Created by Maxmilian Haag on 15.01.2017.
 * @author Maximilian Haag
 *
 * Extended by Niklas Klotz on 21.04.2017
 * 
 * Changes:
 * - Renamed from PushNotificationScheduleBase
 *
 */
public class PushNotificationManager implements PushMessagingInterface {
	
	/** 
	 * The logger. 
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationManager.class);
	
	/**
	 * Google FCM identification credentials read from /src/main/resources/LiveOpCredentials.conf
	 */
	protected static String FCM_SENDER_ID;

	/** The collapse key. */
	protected final String COLLAPSE_KEY = "findLunchDaily";

	/** The number of retries. */
	protected final int NUMBER_OF_RETRIES = 1;

	/** The push repository. */
	@Autowired
	protected PushNotificationRepository pushRepo;

	/** The DailyPushNotification. */
	protected DailyPushNotificationData p;
	
	/** The pushNotification to be send */
	protected JSONObject push;

	/** The restaurants for push count. */
	protected Integer restaurantsForPushCount;

	/** The push kitchen type ids. */
	protected List<Integer> pushKitchenTypeIds;

	/**
	 * Executor service for executing pushes.
	 */
	private ExecutorService executor = Executors.newFixedThreadPool(100);
	

	/**
	 * Initialize base credentials only once for inherited runnables.
	 */
	public PushNotificationManager() {
		if(FCM_SENDER_ID == null) {
			checkLiveOpCredentialsFile();
		}
	}
	
	/**
	 * Load FCM and SNS identification credentials from file at 
	 * src/main/resources/LiveOpCredentials.conf. 
	 */
	private void checkLiveOpCredentialsFile() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();;
		InputStream resourceAsStream = classloader.getResourceAsStream("LiveOpCredentials.conf");
		BufferedReader resReader = null; 
		try {
			resReader = new BufferedReader(new InputStreamReader(resourceAsStream));
			
			//Lines 0-5, data part
			FCM_SENDER_ID = resReader.readLine().split("=")[1];
			
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found.");
		} catch (IOException e) {
			LOGGER.error("I/O error.");
		}
	}
	
	/**
	 * Execute FCM pushNotification.
	 * @author Niklas Klotz.
	 * @throws InterruptedException 
	 */
	@Override
	public void sendFcmNotification(JSONObject p)  {	
			executor.execute(new SendFcmNotification(p));
	}

	/**
	 * Get FCM id.
	 * @return FCM id.
	 */
	public static String getFcmSenderId() {
		return FCM_SENDER_ID;
	}

	/**
	 * Generates a Reservation confirmation push notification.
	 * @param reservation the reservation
	 * @param token the fcm token
	 * @return the notification
	 */
	@SuppressWarnings("unchecked")
	public JSONObject generateReservationConfirm(Reservation reservation, String token){
		JSONObject body = new JSONObject();
		body.put("to", token);

		JSONObject notification = new JSONObject();
		notification.put("title","Deine Bestellung");
		notification.put("body", "Deine Bestellung "+reservation.getReservationNumber()+ " wurd durch das Restaurant "+reservation.getRestaurant().getName()+" bestätigt");

		JSONObject data = new JSONObject();
		data.put("KEY-1", "JSA DATA 1");
		data.put("KEY-2", "JSA DATA ");

		body.put("notification", notification);
		body.put("data", data);

		return body;
	}
	

	/**
	 * Generates a Reservation rejection push notification.
	 * @param reservation the reservation
	 * @param token the fcm token
	 * @return the notification
	 */
	@SuppressWarnings("unchecked")
	public JSONObject generateReservationReject(Reservation reservation, String token){
		JSONObject body = new JSONObject();
		body.put("to", token);

		JSONObject notification = new JSONObject();
		notification.put("title","Deine Bestellung");
		notification.put("body", "Deine Bestellung "+reservation.getReservationNumber()+ " wurde durch das Restaurant "+reservation.getRestaurant().getName()+" abgelehnt. Begründung: "+reservation.getReservationStatus().getStatus());

		JSONObject data = new JSONObject();
		data.put("KEY-1", "JSA DATA 1");
		data.put("KEY-2", "JSA DATA ");

		body.put("notification", notification);
		body.put("data", data);

		return body;
	}
	
	/**
	 * Generates a Reservation rejection push notification.
	 * @param reservation the reservation
	 * @param token the fcm token
	 * @return the notification
	 */
	@SuppressWarnings("unchecked")
	public JSONObject generateReservationNotProcessed(Reservation reservation, String token) {
		JSONObject body = new JSONObject();
		body.put("to", token);

		JSONObject notification = new JSONObject();
		notification.put("title","Deine Bestellung");
		notification.put("body", "Deine Bestellung "+reservation.getReservationNumber()+ " wurde durch das Restaurant "+reservation.getRestaurant().getName()+" nicht rechtzeitig bearbeitet");

		JSONObject data = new JSONObject();
		data.put("KEY-1", "JSA DATA 1");
		data.put("KEY-2", "JSA DATA ");

		body.put("notification", notification);
		body.put("data", data);

		return body;
	}
	
	/**
	 * Generates a push notification for the daily update.
	 * @param p the daily notification
	 * @param restaurantsForPushCount the number of restaurants
	 * @param pushKitchenTypeIds the kitchen type IDs
	 * @param token the token
	 * @return the push notification
	 */
	@SuppressWarnings("unchecked")
	public JSONObject generateFromDaily(DailyPushNotificationData p, Integer restaurantsForPushCount, List<Integer> pushKitchenTypeIds, String token){
		JSONObject body = new JSONObject();
		body.put("to", token);


		JSONObject notification = new JSONObject();
		notification.put("title", p.getTitle());
		notification.put("body", "Im Moment sind " + restaurantsForPushCount + " Restaurants mit leckeren Angeboten in deiner Nähe. Schau doch mal bei uns vorbei.");

		JSONObject data = new JSONObject();
		data.put("KEY-1", "JSA DATA 1");
		data.put("KEY-2", "JSA DATA ");

		body.put("notification", notification);
		body.put("data", data);

		return body;
	}
}
