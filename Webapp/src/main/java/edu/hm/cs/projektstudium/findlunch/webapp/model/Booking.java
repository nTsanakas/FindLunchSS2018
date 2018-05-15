package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

/**
 * The Class Booking. Describes book entrys.
 */
@Entity
@ApiModel(
		description = "Buchungssatz"
)
public class Booking {

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes = "ID")
	private int id;
	
	/** The book id. */
	@ApiModelProperty(notes = "Buchungsnummer")
	private int bookId;
	
	/** The amount. */
	@ApiModelProperty(notes = "Betrag")
	@NumberFormat(style=Style.DEFAULT)
	private float amount;
	
	/** The booking time */
	@ApiModelProperty(notes = "Buchungszeitpunkt")
	private Date bookingTime;

	@ApiModelProperty(notes = "Buchungsgrund")
	@ManyToOne(fetch = FetchType.EAGER)
	private BookingReason bookingReason;
	
	/** The account. */
	@ApiModelProperty(notes = "Account")
	@ManyToOne(fetch = FetchType.EAGER)
	private Account account;
	
	/** The bill. */
	@ApiModelProperty(notes = "Rechnung")
	@ManyToOne(fetch = FetchType.EAGER)
	private Bill bill; 

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
	 * Gets the book id.
	 * @return The book id.
	 */
	public int getBookId() {
		return bookId;
	}

	/**
	 * Sets a new book id.
	 * @param bookId The new book id
	 */
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	/**
	 * Gets the amount of booking.
	 * @return Amount of the booking
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * Sets a new amount to the booking.
	 * @param amount Amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * Gets the bill form the Booking.
	 * @return The bill
	 */
	public Bill getBill() {
		return bill;
	}

	/**
	 * Sets a new bill.
	 * @param bill The bill to set
	 */
	public void setBill(Bill bill) {
		this.bill = bill;
	}

	/**
	 * Gets the booking reason for the booking.
	 * @return The booking reason
	 */
	public BookingReason getBookingReason() {
		return bookingReason;
	}

	/**
	 * Sets the booking reason for the booking.
	 * @param bookingReason Booking reason to set
	 */
	public void setBookingReason(BookingReason bookingReason) {
		this.bookingReason = bookingReason;
	}

	/**
	 * Gets the account for this booking.
	 * @return The account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets the account for this booking.
	 * @param account Account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * Gets the booking time for this booking.
	 * @return The booking Time
	 */
	public Date getBookingTime() {
		return bookingTime;
	}

	/**
	 * Sets the booking time for this booking
	 * @param bookingTime The booking time
	 */
	public void setBookingTime(Date bookingTime) {
		this.bookingTime = bookingTime;
	}
}
