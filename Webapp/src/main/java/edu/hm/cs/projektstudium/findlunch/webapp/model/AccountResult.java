package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The class AccountResult.
 */
@ApiModel(
		description = "Beschreibt das Ergebnis zum Account."
)
public class AccountResult {
	
	/** The account Number.*/
	@ApiModelProperty(notes = "Die Accountnummer")
	private int accountNumber;
	
	/** The sum of Amount.*/
	@ApiModelProperty(notes = "Betragssumme")
	private float sumOfAmount;
	
	/** The customer id .*/
	@ApiModelProperty(notes = "Kundennummer")
	private int customerId;

	/**
	 * Gets the accountNumber
	 * @return The accountNumber
	 */
	public int getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the accountNumber
	 * @param accountNumber AccountNumber to set
	 */
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets the sumOfAmount.
	 * @return the sumOfAmount
	 */
	public float getSumOfAmount() {
		return sumOfAmount;
	}

	/**
	 * Sets the sumOfAmount.
	 * @param sumOfAmount the sumOfAmount
	 */
	public void setSumOfAmount(float sumOfAmount) {
		this.sumOfAmount = sumOfAmount;
	}

	/**
	 * Gets the customerId.
	 * @return the customerId
	 */
	public int getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customerId.
	 * @param customerId The cusomerId to set
	 */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
}
