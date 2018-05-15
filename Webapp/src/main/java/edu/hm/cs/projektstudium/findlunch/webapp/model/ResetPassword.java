package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Class to reset the password.
 * @author oberm
 *
 */
@Entity
@ApiModel(
		description = "Beinhaltet Daten zum Zurücksetzen des Passwortes."
)
public class ResetPassword {
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	/**
	 * The ID.
	 */
	private int id;

	/**
	 * The token.
	 */
	@ApiModelProperty(notes = "Token")
	private String token;

	/**
	 * The date.
	 */
	@ApiModelProperty(notes = "Datum")
	private Date date;

	/**
	 * The user.
	 */
	@ApiModelProperty(notes = "Benutzer")
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;

	/**
	 * Gets the token.
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 * @param token the token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the date.
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date the date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the user
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * @param user the user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the ID.
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the ID.
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
	}
}
