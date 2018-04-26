package edu.hm.cs.projektstudium.findlunch.webapp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The class PushToken
 * The class represents a firebase push token
 * @author Niklas Klotz
 *
 */
@Entity
@Table(name="user_pushtoken")
@ApiModel(
		description = "Beschreibt einen Token für die Push-Mitteilungen."
)
public class PushToken {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The user*/
	@ApiModelProperty(notes = "Benutzer")
	@Column(name="user_id")
	@NotNull
	private int userId;
	
	/** The token*/
	@ApiModelProperty(notes = "FCM-Token")
	@Column(name="fcm_token")
	@NotNull
	private String fcmToken;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return userId;
	}

	public void setUser_id(int user_id) {
		this.userId = user_id;
	}

	public String getFcm_token() {
		return fcmToken;
	}

	public void setFcm_token(String fcm_token) {
		this.fcmToken = fcm_token;
	}
	
}
