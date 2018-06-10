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
 * Defines different types of accounts.
 */
@Entity
@ApiModel(
		description = "Definiert unterschiedliche Accounttypen."
)
public class AccountType {

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes = "ID")
	private int id;
	
	/** The name. */
	@ApiModelProperty(notes = "Name")
	private String name;
	
	/** The accounts. */
	@ApiModelProperty(notes = "Accounts")
	@OneToMany(mappedBy="accountType")
	private List<Account> accounts;

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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets accounts of accountType.
	 * @return List of account
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * Sets a List of accounts.
	 * @param accounts List of accounts to set
	 */
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
}
