package edu.hm.cs.projektstudium.findlunch.webapp.measurement;


/**
 * Interface with all possible measurement cases.
 * To execute, modify PushMeasureBase class.
 * Regard further description at PushMeasureBase class.
 * 
 * Created by Maxmilian Haag on 02.02.2017.
 * @author Maxmilian Haag
 *
 */
public interface PushMeasureInterface {

	/**
	 * Performance FCM measure (high amount of messages).
	 */
	void launchPerformanceFCMMeasure();

	/**
	 * Single speed FCM measure (long data measurement).
	 */
	void launchSpeedFCMMeasure();
	
	/**
	 * Scaled measure, useable with more than one device (up to 10 devices).
	 */
	void launchDeviceScaledFCMMeasure();

}
