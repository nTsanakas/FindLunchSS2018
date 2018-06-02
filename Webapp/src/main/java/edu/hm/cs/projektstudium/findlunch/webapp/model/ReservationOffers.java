package edu.hm.cs.projektstudium.findlunch.webapp.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.ReservationView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The class ReservationOffers
 * The class represnets a list of offers within a reservation.
 * 
 * @author Niklas Klotz
 *
 */
@Entity
@ApiModel(
		description = "Anfragen zur Bestellung."
)
public class ReservationOffers {
	
	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The Reservation */
	@ApiModelProperty(notes = "Reservierung")
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="reservation_id")
	private Reservation reservation;
	
	/** The Offer */
	@ApiModelProperty(notes = "Angebot")
	@ManyToOne(fetch=FetchType.EAGER)
	@JsonView({ReservationView.ReservationRest.class})
	@JoinColumn(name="offer_id")
	private Offer offer;
	
	/** The amount */
	@ApiModelProperty(notes = "Betrag")
	@JsonView({ReservationView.ReservationRest.class})
	private int amount;


	public int getAmount() {
		return amount;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
}
