package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.ReservationView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Class to define the reservation of a product.
 * @author oberm
 *
 */
@Entity
@ApiModel(
		description = "Definiert Reservierung eines Produkts."
)
public class Reservation {
	
	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@JsonView({ReservationView.ReservationRest.class})
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The donation. */
	@ApiModelProperty(notes = "Spende")
	@JsonView({ReservationView.ReservationRest.class})
	private float donation;

	/** The total price. */
	@ApiModelProperty(notes = "Gesamtpreis")
	@JsonView({ReservationView.ReservationRest.class})
	private float totalPrice;

	/** The fee to be payed to PayPal **/
	@JsonView({ReservationView.ReservationRest.class})
	private float fee;

	/** Is used points. */
	@ApiModelProperty(notes = "Benutzte Punkte")
	@JsonView({ReservationView.ReservationRest.class})
	private boolean usedPoints;

	/** Used PayPal for payment */
	@Column(name="used_paypal")
	@JsonView({ReservationView.ReservationRest.class})
	private boolean usedPaypal;

	/** Braintree Transaction ID. Uniquely identifies the transaction, is needed to settle or void it
	 * 	More information: https://developers.braintreepayments.com/reference/general/statuses
	 */
	@Column(name="pp_transaction_id")
	@JsonView({ReservationView.ReservationRest.class})
	private String ppTransactionId;

	/**
	 * Makes sure the PayPal-Transaction has succesfully been fninished (either voided or settled)
	 */
	@Column(name="pp_transaction_finished")
	@JsonView({ReservationView.ReservationRest.class})
	private boolean ppTransactionFinished;
	
	/** Points are Collected by the cusomer */
	@Column(name="points_collected")
	@JsonView({ReservationView.ReservationRest.class})
	private boolean pointsCollected;
	
	/** The points collected by the customer */
	@ApiModelProperty(notes = "Gesammelte Punkte")
	@Column(name="points")
	@JsonView({ReservationView.ReservationRest.class})
	private float points;
	
	/** The reservation number*/
	@ApiModelProperty(notes = "Bestellnummer")
	@JsonView({ReservationView.ReservationRest.class})
	private int reservationNumber;

	/** The user.*/
	@ApiModelProperty(notes = "Benutzer")
	// @JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	/** The offers within the reservation */
	@ApiModelProperty(notes = "Angebote in der Bestellung")
	@OneToMany(mappedBy="reservation", cascade=CascadeType.ALL)
	@JsonView({ReservationView.ReservationRest.class})
	private List<ReservationOffers> reservation_offers;
	
	/** The euroPerPoint.*/
	@ApiModelProperty(notes = "Euro pro Punkt")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="euro_per_point_id")
	private EuroPerPoint euroPerPoint;
	
	/** The restaurant.*/
	@ApiModelProperty(notes = "Restaurant")
	@JsonView({ReservationView.ReservationRest.class})
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="restaurant_id")
	private Restaurant restaurant;
	
	/** The bill.*/
	@ApiModelProperty(notes = "Rechnung")
	@ManyToOne(fetch=FetchType.EAGER)
	//@JsonView({ReservationView.ReservationRest.class})
	private Bill bill;

	/** The reservationStatus.*/
	@ApiModelProperty(notes = "Reservierungsstatus")
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonView({ReservationView.ReservationRest.class})
	@JoinColumn(name="reservation_status_id")
	private ReservationStatus reservationStatus;
	
	/** The collect_time. */
	@ApiModelProperty(notes = "Zeit")
	@JsonView({ReservationView.ReservationRest.class})
	@Column(name="collect_time")
	private Date collectTime;
	
	/** The reservation time. */
	@ApiModelProperty(notes = "Bestellzeit empfangen")
	//@JsonView({ReservationView.ReservationRest.class})
	@Column(name="timestamp_received")
	private Date timestampReceived;
	
	/** The reservation time. */
	@ApiModelProperty(notes = "Geantwortete Bestellzeit")
	//@JsonView({ReservationView.ReservationRest.class})
	@Column(name="timestamp_responded")
	private Date timestampResponded;

	/**
	 * 	Payment nonce, used when payment is done via Paypal. This is only in here to be mapped in an incoming REST call.
	 * 	As this is one-time use sensitive information it will not be stored in the database and is therefore @Transient.
	 */
	@Transient
	@JsonView({ReservationView.ReservationRest.class})
	private String nonce;

	/**
	 * Gets the euroPerPoint.
	 * @return The euroPerPoint
	 */
	public EuroPerPoint getEuroPerPoint() {
		return euroPerPoint;
	}
	
	/**
	 * Sets the euroPerPoint
	 * @param euroPerPoint The new euroPerPoint
	 */
	public void setEuroPerPoint(EuroPerPoint euroPerPoint) {
		this.euroPerPoint = euroPerPoint;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
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
	 * Gets the user.
	 * @return The user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the new user.
	 * @param user The user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the donation.
	 * @return The donation
	 */
	public float getDonation() {
		return donation;
	}

	/**
	 * Sets the donation.
	 * @param donation The new donation to set
	 */
	public void setDonation(float donation) {
		this.donation = donation;
	}

	/**
	 * Gets the total price.
	 * @return The total price
	 */
	public float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Sets the total price.
	 * @param totalPrice The new total price
	 */
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * Gets the fee for PayPal.
	 * @return fee The fee
	 */
	public float getFee() {	return fee;	}

	/**
	 * Sets the fee for PayPal.
	 * @param fee
	 */
	public void setFee(float fee) {	this.fee = fee;	}

	/**
	 * Checks if points are used.
	 * @return true, if points are used
	 */
	public boolean isUsedPoints() {
		return usedPoints;
	}

	/**
	 * Set the flag of used points.
	 * @param usedPoints true, if points are used
	 */
	public void setUsedPoints(boolean usedPoints) {
		this.usedPoints = usedPoints;
	}

	/**
	 * Checks if PayPal was used.
	 * @return true, if PayPal was used
	 */
	public boolean getUsedPaypal() {return usedPaypal;}

	/**
	 * Set the flag of used PayPal
	 * @param usedPaypal true, if PayPal was used
	 */
	public void setUsedPaypal(boolean usedPaypal) {this.usedPaypal = usedPaypal;}

	/**
	 * Get the transaction ID.
	 * @return braintree transaction ID
	 */
	public String getPpTransactionId() { return ppTransactionId; }

	/**
	 * Set the transaction ID.
	 * @param ppTransactionId the new transaction ID.
	 */
	public void setPpTransactionId(String ppTransactionId) { this.ppTransactionId = ppTransactionId; }

	/**
	 * Get if the transaction is finished.
	 * @return Is braintree transaction finished
	 */
	public boolean isPpTransactionFinished() { return ppTransactionFinished; }

	/**
	 * Set the transaction finished.
	 * @param ppTransactionFinished the new transaction status.
	 */
	public void setPpTransactionFinished(boolean ppTransactionFinished) { this.ppTransactionFinished = ppTransactionFinished; }

	/**
	 * Gets the restaurant.
	 * @return The restaurant
	 */
	public Restaurant getRestaurant() {
		return restaurant;
	}

	/**
	 * Sets the restaurant.
	 * @param restaurant The restaurant to set
	 */
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * Gets the bill.
	 * @return The bill
	 */
	public Bill getBill() {
		return bill;
	}

	/**
	 * Sets the new bill to reservation.
	 * @param bill The bill to set
	 */
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	
	/**
	 * Gets the reservation number.
	 * @return The reservation number
	 */
	public int getReservationNumber() {
		return reservationNumber;
	}

	/**
	 * Sets the new reservation number.
	 * @param reservationNumber The new reservation number to set
	 */
	public void setReservationNumber(int reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	/**
	 * Gets the reservation offers.
	 * @return the reservation offers
	 */
	public List<ReservationOffers> getReservation_offers() {
		return reservation_offers;
	}

	/**
	 * Sets the reservation offers.
	 * @param reservation_offers the reservation offers
	 */
	public void setReservation_offers(List<ReservationOffers> reservation_offers) {
		this.reservation_offers = reservation_offers;
	}

	/**
	 * @return the reservationStatus
	 */
	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}

	/**
	 * Sets the reservation status
	 * @param reservationStatus the reservationStatus to set
	 */
	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	/**
	 * @return the collectTime
	 */
	public Date getCollectTime() {
		return collectTime;
	}

	/**
	 * @param collectTime the collectTime to set
	 */
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	/**
	 * @return the timestampReceived
	 */
	public Date getTimestampReceived() {
		return timestampReceived;
	}

	/**
	 * @param timestampReceived the timestampReceived to set
	 */
	public void setTimestampReceived(Date timestampReceived) {
		this.timestampReceived = timestampReceived;
	}

	/**
	 * @return the timestampResponded
	 */
	public Date getTimestampResponded() {
		return timestampResponded;
	}

	/**
	 * @param timestampResponded the timestampResponded to set
	 */
	public void setTimestampResponded(Date timestampResponded) {
		this.timestampResponded = timestampResponded;
	}

	/**
	 * @return boolean true when Reservation is Confirmed
	 */
	public boolean isConfirmed(){
		return reservationStatus.getKey() == ReservationStatus.RESERVATION_KEY_CONFIRMED;
	}
	
	/**
	 * @return boolean true when Reservation is Rejected
	 */
	public boolean isRejected(){
		return reservationStatus.getKey() == ReservationStatus.RESERVATION_KEY_REJECTED;
	}
	
	/**
	 * @return boolean true when Reservation is Unprocessed
	 */
	public boolean isUnprocessed(){
		return reservationStatus.getKey() == ReservationStatus.RESERVATION_KEY_UNPROCESSED;
	}

	public boolean isPointsCollected() {
		return pointsCollected;
	}

	public void setPointsCollected(boolean pointsCollected) {
		this.pointsCollected = pointsCollected;
	}

	public float getPoints() {
		return points;
	}

	public void setPoints(float points) {
		this.points = points;
	}

	/**
	 * Gets the nonce for PayPal Payment.
	 * @return The nonce
	 */
	public String getNonce() {return nonce;	}
}
