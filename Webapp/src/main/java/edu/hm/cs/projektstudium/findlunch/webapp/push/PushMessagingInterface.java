package edu.hm.cs.projektstudium.findlunch.webapp.push;

import org.json.simple.JSONObject;

/**
 * Base interface / abstract API for sending push-based notifications.
 * 
 * Implementations:
 * **Google Firebase Cloud Messaging Service
 *  
 * Created by Maxmilian Haag on 12.12.2016.
 * @author Maxmilian Haag
 * 
 * Extended by Niklas Klotz 21.04.2017.
 * @author Niklas Klotz
 *
 */
public interface PushMessagingInterface {
	
	/**
	 * Sends FCM pushNotification to a customer.
	 * @author Niklas Klotz.
	 * @param p The push-notification to be sent.
	 * @throws InterruptedException 
	 */
	void sendFcmNotification(JSONObject p);
}
