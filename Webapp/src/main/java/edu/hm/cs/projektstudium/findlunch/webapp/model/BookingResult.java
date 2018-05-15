package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The Class BookingResult. Descripbes requirements for bookings.
 */
@ApiModel(
		description = "Beschreibt Forderungen zu Buchungen."
)
public class BookingResult {

	/** The sum of Claims */
	@ApiModelProperty(notes = "Forderungssumme")
	private float allClaim;
	
	/** The paid Claim */
	@ApiModelProperty(notes = "Bezahlte Forderung")
	private float paidClaim;
	
	/** The not paid Claim */
	@ApiModelProperty(notes = "Offene Forderung")
	private float notPaidClaim;
	
	/** The start date */
	@ApiModelProperty(notes = "Beginn")
	@DateTimeFormat(pattern="dd.MM.yyy HH:mm")
	//@NotNull(message="{offer.startDate.notNull}")
	private Date startDate;
	
	/** The end date */
	@ApiModelProperty(notes = "Ende")
	@DateTimeFormat(pattern="dd.MM.yyy HH:mm")
	//@NotNull(message="{offer.endDate.notNull}")
	private Date endDate;

	/**
	 * Gets the sum of Claims.
	 * @return All Claims
	 */
	public float getAllClaim() {
		return allClaim;
	}

	/**
	 * Sets the sum of claims.
	 * @param allClaim Claim to set
	 */
	public void setAllClaim(float allClaim) {
		this.allClaim = allClaim;
	}

	/**
	 * Gets the sum of paid claims.
	 * @return Paid claims
	 */
	public float getPaidClaim() {
		return paidClaim;
	}

	/**
	 * Sets the Paid Claim
	 * @param paidClaim PaidClaim to set
	 */
	public void setPaidClaim(float paidClaim) {
		this.paidClaim = paidClaim;
	}

	/**
	 * Gets the sum of not paid claims.
	 * @return sum of not paid claims
	 */
	public float getNotPaidClaim() {
		return notPaidClaim;
	}

	/**
	 * Sets the not paid Claim.
	 * @param notPaidClaim NotPaidClaim to set
	 */
	public void setNotPaidClaim(float notPaidClaim) {
		this.notPaidClaim = notPaidClaim;
	}

	/**
	 * Gets the start date.
	 * @return The start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 * @param startDate StartDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 * @return The end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date
	 * @param endDate The end date to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
