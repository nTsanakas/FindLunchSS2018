package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.PushNotificationView;


/**
 * The Class DailyPushNotificationData.
 * 
 * Database entries extended to sns_token and fcm_token for better overview and extension possibilities.
 */
@Entity
@Table(name="push_notification")
@ApiModel(
		description = "Repräsentiert eine Push-Benachrichtigung."
)
@Getter
@Setter
public class DailyPushNotificationData implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue 
	@JsonView(PushNotificationView.PushNotificationRest.class)
	private int id;
	
	/** The title. */
	@ApiModelProperty(notes = "Titel")
	@JsonView(PushNotificationView.PushNotificationRest.class)
	private String title;
 
	/** The fcm token. */
	@ApiModelProperty(notes = "FCM-Token")
	@Lob
	@Column(name="fcm_token")
	private String fcmToken;
	
	/** The sns token. */
	@ApiModelProperty(notes = "SNS-Token")
	@Lob
	@Column(name="sns_token")
	private String snsToken;

	/** The latitude. */
	@ApiModelProperty(notes = "Breitengrad")
	private float latitude;

	/** The longitude. */
	@ApiModelProperty(notes = "Längengrad")
	private float longitude;


	/** The radius. */
	@ApiModelProperty(notes = "Radius")
	private int radius;

	/** The user. */
	//bi-directional many-to-one association to User
	@ApiModelProperty(notes = "Benutzer")
	@ManyToOne
	private User user;
	
	/** The day of weeks. */
	@ApiModelProperty(notes = "Wochentag")
	//bi-directional many-to-many association to DayOfWeek
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name="push_notification_has_day_of_week"
		, joinColumns={
			@JoinColumn(name="push_notification_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="day_of_week_id")
			}
		)
	@JsonView(PushNotificationView.PushNotificationRest.class)
	private List<DayOfWeek> dayOfWeeks;

	/** The kitchen types. */
	@ApiModelProperty(notes = "Küchentyp")
	//bi-directional many-to-many association to KitchenType
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name="push_notification_has_kitchen_type"
		, joinColumns={
			@JoinColumn(name="push_notification_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="kitchen_type_id")
			}
		)
	@JsonView(PushNotificationView.PushNotificationRest.class)
	private List<KitchenType> kitchenTypes;

	/**
	 * Instantiates a new push notification.
	 */
	public DailyPushNotificationData() {
	}
}