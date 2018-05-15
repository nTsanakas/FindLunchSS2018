package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.ReservationView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;

/**
 * The Class Restaurant.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(
		description = "Ein Restaurant."
)
@Getter
@Setter
public class Restaurant implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView({RestaurantView.RestaurantRest.class, ReservationView.ReservationRest.class})
	private int id;

	/** The city. */
	@ApiModelProperty(notes = "Stadt")
	@JsonView({RestaurantView.RestaurantRest.class, ReservationView.ReservationRest.class})
	@NotBlank(message = "{restaurant.city.notBlank}")
	@Size(min=2, max=60, message= "{restaurant.city.sizeError}")
	@Pattern(regexp = "[\\p{L} ]*", message="{restaurant.city.patternMismatch}")
	private String city;

	/** The email. */
	@ApiModelProperty(notes = "E-Mail-Adresse")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotBlank(message = "{restaurant.email.notBlank}")
	@Size(min=2, max=60, message= "{restaurant.email.sizeError}")
	private String email;

	/** The location latitude. */
	@ApiModelProperty(notes = "Breitengrad")
	@Column(name = "location_latitude")
	@JsonView(RestaurantView.RestaurantRest.class)
	private float locationLatitude;

	/** The location longitude. */
	@ApiModelProperty(notes = "Längengrad")
	@Column(name = "location_longitude")
	@JsonView(RestaurantView.RestaurantRest.class)
	private float locationLongitude;

	/** The name. */
	@ApiModelProperty(notes = "Name")
	@JsonView({RestaurantView.RestaurantRest.class, ReservationView.ReservationRest.class})
	@NotBlank(message = "{restaurant.name.notBlank}")
	@Size(min=2, max=60, message= "{restaurant.name.sizeError}")
	@Pattern(regexp = "[\\p{L}0-9-&´`'\"(). ]*", message="{restaurant.name.patternMismatch}")
	private String name;

	/** The phone. */
	@ApiModelProperty(notes = "Telefonnummer")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotBlank(message = "{restaurant.phone.notBlank}")
	@Size(min=3, max=60, message= "{restaurant.phone.sizeError}")
	@Pattern(regexp = "[0-9+/()\\- ]{1,}", message="{restaurant.phone.patternMismatch}")
	private String phone;

	/** The street. */
	@ApiModelProperty(notes = "Straße")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotBlank(message = "{restaurant.street.notBlank}")
	@Size(min=2, max=60, message= "{restaurant.street.sizeError}")
	private String street;

	/** The street number. */
	@ApiModelProperty(notes = "Hausnummer")
	@Column(name = "street_number")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotBlank(message = "{restaurant.streetNumber.notBlank}")
	@Pattern(regexp = "[1-9]{1}[0-9]{0,3}[a-zA-Z]?(-[1-9]{1}[0-9]{0,3}[a-zA-Z]?)?", message="{restaurant.streetNumber.patternMismatch}")
	@Size(min=1, max=11, message= "{restaurant.streetNumber.sizeError}")
	private String streetNumber;

	/** The url. */
	@ApiModelProperty(notes = "Webseite")
	@JsonView(RestaurantView.RestaurantRest.class)
	@URL(message = "{restaurant.urlInvalid}")
	@Size(max=60, message= "{restaurant.url.sizeError}")
	private String url;

	/** The zip. */
	@ApiModelProperty(notes = "Postleitzahl")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotBlank(message = "{restaurant.zip.notBlank}")
	@NumberFormat(style = Style.NUMBER, pattern = "#####")
	@Size(min=5, max=5, message = "{restaurant.zip.size}")
	@Pattern(regexp = "^([0]{1}[1-9]{1}|[1-9]{1}[0-9]{1})[0-9]{3}$", message="{restaurant.zip.patternMismatch}")
	private String zip;

	/** The offers. */
	@ApiModelProperty(notes = "Angebote")
	// bi-directional many-to-one association to Offer
	@OneToMany(mappedBy = "restaurant")
	private List<Offer> offers;

	/** The country. */
	@ApiModelProperty(notes = "Land")
	// bi-directional many-to-one association to Country
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "country_code")
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotNull(message = "{restaurant.country.notNull}")
	private Country country;

	/** The kitchen types. */
	@ApiModelProperty(notes = "Küchentypen")
	// bi-directional many-to-many association to KitchenType
	@ManyToMany
	@JoinTable(name = "restaurant_has_kitchen_type", joinColumns = {
	@JoinColumn(name = "restaurant_id") }, inverseJoinColumns = { @JoinColumn(name = "kitchen_type_id") })
	@JsonView(RestaurantView.RestaurantRest.class)
	@NotEmpty(message = "{restaurant.kitchenType.notEmpty}")
	private List<KitchenType> kitchenTypes;

	/** The restaurant type. */
	@ApiModelProperty(notes = "Restauranttyp")
	// bi-directional many-to-one association to RestaurantType
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_type_id")
	@JsonView(RestaurantView.RestaurantRest.class)
	private RestaurantType restaurantType;

	/** The time schedules. */
	@ApiModelProperty(notes = "Öffnungszeiten")
	// bi-directional many-to-one association to TimeSchedule
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonView(RestaurantView.RestaurantRest.class)
	private List<TimeSchedule> timeSchedules;

	/** The admins. */
	@ApiModelProperty(notes = "Administratoren")
	// bi-directional many-to-one association to User
	@JsonIgnore
	@OneToMany(mappedBy = "restaurant",fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private List<User> admins;
	
	
	/** The fav users. */
	@ApiModelProperty(notes = "Von Benutzern als Favorit markiert.")
	@ManyToMany
	@JoinTable(name = "favorites", joinColumns = {
	@JoinColumn(name = "restaurant_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	private List<User> favUsers;
	
	/** The default Logo. */
	@ApiModelProperty(notes = "Logo")
	@Transient
	@JsonView(RestaurantView.RestaurantRest.class)
	private RestaurantLogo defaultLogo;
	
	/** The distance. */
	@ApiModelProperty(notes = "Entfernung")
	@Transient
	@JsonView(RestaurantView.RestaurantRest.class)
	private int distance;
	
	/** Is favorite restaurant.*/
	@ApiModelProperty(notes = "Favorit?")
	@Transient
	@JsonView(RestaurantView.RestaurantRest.class)
	private boolean isFavorite;

	/** The points of restaurant.*/
	@ApiModelProperty(notes = "Punkte")
	@OneToMany(mappedBy="compositeKey.restaurant", cascade= CascadeType.ALL)
	private List<Points>  restaurantPoints;
	
	/** The actual point.*/
	@ApiModelProperty(notes = "Aktuelle Punkte")
	@Transient
	@JsonView(RestaurantView.RestaurantRest.class)
	private int actualPoints;
	
	/** The reservations.*/
	@ApiModelProperty(notes = "Bestellungen")
	@OneToMany(mappedBy="restaurant")
	List<Reservation> reservation;
	
	/** The uuid of the restaurant.*/
	@ApiModelProperty(notes = "UUID")
	private String restaurantUuid;

	/** The qr-code in bytes.*/
	@ApiModelProperty(notes = "QR-Code")
	@Lob
	private byte[] qrUuid;
	
	/** The qr-code in base64.*/
	@ApiModelProperty(notes = "Base64-kodierter QR-Code")
	@Transient
	private String base64Encoded;
	
	/** The customer id.*/
	@ApiModelProperty(notes = "Kundennummer")
	private int customerId;
	
	/** The donations of the restaurant.*/
	@ApiModelProperty(notes = "Spenden für das Restaurant")
	@OneToMany(mappedBy="restaurant", fetch = FetchType.EAGER)
	private List<DonationPerMonth> donations;
	
	/** The bills of the restaurant.*/
	@ApiModelProperty(notes = "Rechnungen")
	@OneToMany(mappedBy="restaurant")
	private List<Bill> bills;

	/** The offer photos. */
	@ApiModelProperty(notes = "Angebotsbilder")
	//bi-directional many-to-one association to OfferPhoto
	@OneToMany(mappedBy="restaurant", cascade=CascadeType.ALL, orphanRemoval=true )
	private List<RestaurantLogo> restaurantLogos;
	
	@JsonView(RestaurantView.RestaurantRest.class)
	@ApiModelProperty(notes = "Aktuell geöffnet")
	@Transient
	private boolean currentlyOpen;

	@Column(name = "swa_offer_modify_permission")
	private boolean offerModifyPermission;

	@Column(name = "swa_blocked")
	private boolean blocked;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "swa_sales_person_id")
	private SalesPerson salesPerson;
	
	/**
	 * Instantiates a new restaurant.
	 */
	public Restaurant() {
		this.admins = new ArrayList<User>();

	}
	
	public RestaurantLogo getDefaultLogo() {
		if(this.restaurantLogos != null && this.restaurantLogos.size() > 0)
			defaultLogo = this.restaurantLogos.get(0);
		
		return defaultLogo;
	}

	/**
	 * Adds the offer.
	 *
	 * @param offer the offer
	 * @return the offer
	 */
	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setRestaurant(this);

		return offer;
	}

	/**
	 * Removes the offer.
	 *
	 * @param offer the offer
	 * @return the offer
	 */
	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setRestaurant(null);

		return offer;
	}

	/**
	 * Sets the time schedules.
	 *
	 * @param timeSchedules the new time schedules
	 */
	public void setTimeSchedules(List<TimeSchedule> timeSchedules) {
		if(this.timeSchedules == null)
			this.timeSchedules = timeSchedules;
		else
		{
			this.timeSchedules.clear();
			if(timeSchedules != null)
				this.timeSchedules.addAll(timeSchedules);
		}
	}

	/**
	 * Adds the time schedule.
	 *
	 * @param timeSchedule the time schedule
	 * @return the time schedule
	 */
	public TimeSchedule addTimeSchedule(TimeSchedule timeSchedule) {
		getTimeSchedules().add(timeSchedule);
		timeSchedule.setRestaurant(this);

		return timeSchedule;
	}

	/**
	 * Removes the time schedule.
	 *
	 * @param timeSchedule the time schedule
	 * @return the time schedule
	 */
	public TimeSchedule removeTimeSchedule(TimeSchedule timeSchedule) {
		getTimeSchedules().remove(timeSchedule);
		timeSchedule.setRestaurant(null);

		return timeSchedule;
	}

	/**
	 * Adds an admin.
	 *
	 * @param admin the admin
	 * @return the user
	 */
	public User addAdmin(User admin) {
		getAdmins().add(admin);
		admin.setRestaurant(this);

		return admin;
	}

	/**
	 * Removes an admin.
	 *
	 * @param admin the admin
	 * @return the user
	 */
	public User removeAdmin(User admin) {
		getAdmins().remove(admin);
		admin.setRestaurant(null);

		return admin;
	}

	/**
	 * Add a new donationPerMonth.
	 * @param donationPerMonth donation to add
	 * @return added donation
	 */
	public DonationPerMonth addDonationPerMonth(DonationPerMonth donationPerMonth) {
		getDonations().add(donationPerMonth);
		donationPerMonth.setRestaurant(this);

		return donationPerMonth;
	}
	
	/**
	 * Adds the restaurant logo.
	 *
	 * @param restaurantLogo the restuarant logo
	 * @return the restaurant logo
	 */
	public RestaurantLogo addRestaurantLogo(RestaurantLogo restaurantLogo) {
		
		if(null==getRestaurantLogos()){
			this.restaurantLogos = new ArrayList<RestaurantLogo>();
		}
		
		getRestaurantLogos().add(restaurantLogo);
		restaurantLogo.setRestaurant(this);

		return restaurantLogo;
	}

	/**
	 * Removes the restaurant logo.
	 *
	 * @param restaurantLogo the restaurant logo
	 * @return the restaurant logo
	 */
	public RestaurantLogo removeRestaurantLogo(RestaurantLogo restaurantLogo) {
		getRestaurantLogos().remove(restaurantLogo);
		restaurantLogo.setRestaurant(null);

		return restaurantLogo;
	}

	/**
	 * @return the isOpen
	 */
	public boolean getCurrentlyOpen() {
		
		int openHour = 0;
		int openMin = 0;
		int closeHour = 0;
		int closeMin = 0;
		
		for (TimeSchedule timeSchedule : this.timeSchedules) {
			Date actuallyDate = new Date();
			if(actuallyDate.getDay()+1==timeSchedule.getDayOfWeek().getDayNumber()){
				
				// check if there is a openingtime for the day
				List<OpeningTime> openingTimes = timeSchedule.getOpeningTimes();
				if(!openingTimes.isEmpty()){
					for(OpeningTime opening : openingTimes) {
						if(opening.getTimeSchedule().getId()==timeSchedule.getId()){
							openHour = opening.getOpeningTime().getHours();
							openMin = opening.getOpeningTime().getMinutes();
							closeHour = opening.getClosingTime().getHours();
							closeHour = opening.getClosingTime().getMinutes();
						}
						else {
							openHour = timeSchedule.getOfferStartTime().getHours();
							openMin = timeSchedule.getOfferStartTime().getMinutes();
							closeHour = timeSchedule.getOfferEndTime().getHours();
							closeMin = timeSchedule.getOfferEndTime().getMinutes();
						}
					}
				}
				else {
					openHour = timeSchedule.getOfferStartTime().getHours();
					openMin = timeSchedule.getOfferStartTime().getMinutes();
					closeHour = timeSchedule.getOfferEndTime().getHours();
					closeMin = timeSchedule.getOfferEndTime().getMinutes();
				}
				
				int nowHour = actuallyDate.getHours();
				int nowMin = actuallyDate.getMinutes();
				
				if(openHour > nowHour || (openHour == nowHour && openMin > nowMin )){
					return false;
				}
				
				if(closeHour < nowHour || (closeHour == nowHour && closeMin < nowMin )){
					return false;
				}
				return true;
			}
		}
		return false;
	}
}