package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.PushNotificationView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Defines different kitchen types.
 */
@Entity
@Table(name="kitchen_type")
@ApiModel(
		description = "Definiert verschiedene Küchentypen."
)
public class KitchenType {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView({RestaurantView.RestaurantRest.class, PushNotificationView.PushNotificationRest.class})
	private int id;

	/** The name. */
	@ApiModelProperty(notes = "Name")
	@JsonView({RestaurantView.RestaurantRest.class, PushNotificationView.PushNotificationRest.class})
	private String name;
	
	/** The push notifications. */
	//bi-directional many-to-many association to PushNotification
	@ApiModelProperty(notes = "Push-Benachrichtigungen")
	@ManyToMany(mappedBy="kitchenTypes")
	private List<DailyPushNotificationData> pushNotifications;

	/** The restaurants. */
	@ApiModelProperty(notes = "Restaurants")
	//bi-directional many-to-many association to Restaurant
	@ManyToMany(mappedBy="kitchenTypes")
	@JsonIgnore
	private List<Restaurant> restaurants;

	/**
	 * Instantiates a new kitchen type.
	 */
	public KitchenType() {
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the push notifications.
	 *
	 * @return the push notifications
	 */
	public List<DailyPushNotificationData> getPushNotifications() {
		return this.pushNotifications;
	}

	/**
	 * Sets the push notifications.
	 *
	 * @param pushNotifications the new push notifications
	 */
	public void setPushNotifications(List<DailyPushNotificationData> pushNotifications) {
		this.pushNotifications = pushNotifications;
	}

	/**
	 * Gets the restaurants.
	 *
	 * @return the restaurants
	 */
	public List<Restaurant> getRestaurants() {
		return this.restaurants;
	}

	/**
	 * Sets the restaurants.
	 *
	 * @param restaurants the new restaurants
	 */
	public void setRestaurants(List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

}