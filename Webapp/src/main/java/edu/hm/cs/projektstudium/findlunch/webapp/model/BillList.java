package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;

/**
 * Combines multiple bills to a list of bills.
 */
@ApiModel(
		description = "Fasst mehrere Rechnungen zu einer Liste zusammen."
)
public class BillList {
	
	/** The list of bills */
	@ApiModelProperty(notes = "Rechnungsliste")
	private ArrayList<Bill> bills = new ArrayList<Bill>();

	/**
	 * Gets the list of bills.
	 * @return List of bill
	 */
	public ArrayList<Bill> getBills() {
		return bills;
	}

	/**
	 * Sets the list of bills.
	 * @param bills List of bill to set
	 */
	public void setBills(ArrayList<Bill> bills) {
		this.bills = bills;
	}
}
