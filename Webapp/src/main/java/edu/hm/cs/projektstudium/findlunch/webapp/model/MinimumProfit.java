package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * The Class MinimumProfit. Defines the minimal profit for a restaurant.
 */
@Entity
@ApiModel(
		description = "Definiert den minimalen Gewinn für ein Restaurant."
)
public class MinimumProfit {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The profit.*/
	@ApiModelProperty(notes = "Gewinn")
	private float profit;
	
	/** The bills.*/
	@ApiModelProperty(notes = "Minimaler Gewinn")
	@OneToMany(mappedBy="minimumProfit")
	private List<Bill> bills;

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
	 * Gets the profit.
	 * @return the profit
	 */
	public float getProfit() {
		return profit;
	}

	/**
	 * Sets the new profit.
	 * @param profit Profit to set
	 */
	public void setProfit(float profit) {
		this.profit = profit;
	}

	/**
	 * Gets the bills.
	 * @return The bills
	 */
	public List<Bill> getBills() {
		return bills;
	}

	/**
	 * Sets the bills.
	 * @param bills The bills to set
	 */
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
}
