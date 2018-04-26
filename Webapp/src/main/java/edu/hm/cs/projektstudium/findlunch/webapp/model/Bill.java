package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * The Class Bill.
 */
@Entity
@ApiModel(
		description = "Beschreibt eine Rechnung zu einem Kauf."
)
public class Bill {

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes = "ID")
	private int id;
	
	/** The bill number. */
	@ApiModelProperty(notes = "Rechnungsnummer")
	private String billNumber;
	
	/** The start date. */
	@ApiModelProperty(notes = "Beginn")
	private Date startDate;
	
	/** The end date. */
	@ApiModelProperty(notes = "Ende")
	private Date endDate;
	
	/** The total price. */
	@ApiModelProperty(notes = "Gesamtpreis")
	private float totalPrice;
	
	/** Is paid. */
	@ApiModelProperty(notes = "Bezahlt?")
	private boolean paid;
	
	/**
	 * The minimumProfit.
	 */
	@ApiModelProperty(notes = "Geringster Gewinn")
	@ManyToOne(fetch = FetchType.EAGER)
	private MinimumProfit minimumProfit;
	
	/**
	 * List of reservation for this bill.
	 */
	@ApiModelProperty(notes = "Reservierungsliste für diese Rechnung")
	@OneToMany(mappedBy="bill")
	private List<Reservation> reservations;
	
	/**
	 * The restaurant.
	 */
	@ApiModelProperty(notes = "Restaurant")
	@ManyToOne(fetch = FetchType.EAGER)
	private Restaurant restaurant;
	
	/**
	 * The donationPerMonth.
	 */
	@ApiModelProperty(notes = "Monatliche Spenden")
	@OneToMany(mappedBy= "bill")
	private List<DonationPerMonth> ListOfDonationPerMonth;
	
	/**
	 * The pdf in bytes.
	 */
	@ApiModelProperty(notes = "PDF")
	@Lob
	private byte[] billPdf;
	
	/**
	 * The bookings of the bill.
	 */
	@ApiModelProperty(notes = "Buchungen")
	@OneToMany(mappedBy="bill")
	private List<Booking> bookings;

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
	 * Gets the billNumber.
	 * @return The billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	/**
	 * Sets the billNumber
	 * @param billNumber The billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	/**
	 * Gets the start date.
	 * @return start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the startDate.
	 * @param startDate StartDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the endDate.
	 * @return The endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the endDate
	 * @param endDate The endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Checks if a bill is paid.
	 * @return true, if it is paid
	 */
	public boolean isPaid() {
		return paid;
	}

	/**
	 * Sets if a bill is paid or not.
	 * @param paid true, for paid
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	/**
	 * Gets the miniumProfig for this bill.
	 * @return The minimumProfit
	 */
	public MinimumProfit getMinimumProfit() {
		return minimumProfit;
	}

	/**
	 * Sets the minimumProfit.
	 * @param minimumProfit The minimumProfit to set
	 */
	public void setMinimumProfit(MinimumProfit minimumProfit) {
		this.minimumProfit = minimumProfit;
	}

	/**
	 * Gets the List of reservation for this bill.
	 * @return List of reservations
	 */
	public List<Reservation> getReservations() {
		return reservations;
	}

	/**
	 * Sets the list of reservations for this bill.
	 * @param reservations List of reservations to set
	 */
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	/**
	 * Gets the restaurant from this bill.
	 * @return The restaurant
	 */
	public Restaurant getRestaurant() {
		return restaurant;
	}

	/**
	 * Sets the restaurant from this bill.
	 * @param restaurant The restaurant
	 */
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * Gets the pdf as bytes.
	 * @return pdf as bytes
	 */
	public byte[] getBillPdf() {
		return billPdf;
	}

	/**
	 * Sets the pdf as bytes.
	 * @param billPdf The bytes to set
	 */
	public void setBillPdf(byte[] billPdf) {
		this.billPdf = billPdf;
	}

	/**
	 * Gets the list of donationPerMonths
	 * @return List of donation
	 */
	public List<DonationPerMonth> getListOfDonationPerMonth() {
		return ListOfDonationPerMonth;
	}

	/**
	 * Sets the list of donationPerMonth
	 * @param listOfDonationPerMonth List of donationPerMonth to set
	 */
	public void setListOfDonationPerMonth(List<DonationPerMonth> listOfDonationPerMonth) {
		ListOfDonationPerMonth = listOfDonationPerMonth;
	}

	/**
	 * Gets the booking of this bill.
	 * @return List of booking
	 */
	public List<Booking> getBookings() {
		return bookings;
	}

	/**
	 * Sets the bookings of this bill.
	 * @param bookings List of booking to set
	 */
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	/**
	 * Gets the totalPrice for this bill.
	 * @return The totalPrice.
	 */
	public float getTotalPrice() {
		return totalPrice;
	}

	/**
	 * Sets the totalPrice for this bill.
	 * @param totalPrice The totalPrice to set
	 */
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
}
