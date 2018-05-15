package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class User.
 */
@Entity
// Inherited property needs to be ignored or else the RegisterUserRestController integration test is not working when passing a user object to the request.
@JsonIgnoreProperties({"authorities"})
@ApiModel(
		description = "Ein Benutzer des FindLunch-Systems."
)
@Getter
@Setter
public class User implements UserDetails {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The password. */
	@ApiModelProperty(notes = "Passwort")
	@NotBlank(message="{user.passwordEmpty}")
	private String password;
	
	/** The passwordconfirm. */
	@ApiModelProperty(notes = "Passwort-Bestätigung")
	@Transient
	private String passwordconfirm;
	
	/** The username. */
	@ApiModelProperty(notes = "Benutzername")
	@NotBlank(message="{user.usernameEmpty}")
	private String username;
	
	//private String fcmId;

	/**
	 * A user object has a Captcha object.
	 */
	@ApiModelProperty(notes = "Captcha-Objekt des Benutzers")
	@Transient
	private Captcha captcha;

	/** The favorites. */
	//bi-directional many-to-many association to Restaurant
	@ApiModelProperty(notes = "Favoriten")
	@ManyToMany
	@JoinTable(
		name="favorites"
		, joinColumns={
			@JoinColumn(name="user_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="restaurant_id")
			}
		)
	private List<Restaurant> favorites;
	
	/** The push notifications. */
	//bi-directional many-to-one association to PushNotification
	@ApiModelProperty(notes = "Push-Notifikationen")
	@OneToMany(mappedBy="user")
	private List<DailyPushNotificationData> pushNotifications;

	/** The restaurant. */
	@ApiModelProperty(notes = "Restaurants")
	//bi-directional many-to-one association to Restaurant
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Restaurant restaurant;

	/** The user type. */
	@ApiModelProperty(notes = "Benutzergruppe")
	//bi-directional many-to-one association to UserType
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_type_id")
	private UserType userType;
	
	/** The reservations.*/
	@ApiModelProperty(notes = "Reservierungen")
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private List<Reservation> reservation; 
	
	/** The points of the user.*/
	@ApiModelProperty(notes = "Punkte")
	@OneToMany(mappedBy="compositeKey.user", cascade=CascadeType.ALL)
	private List<Points> userPoints;
	
	/** The account.*/
	@ApiModelProperty(notes = "Account")
	@ManyToOne
	private Account account;

	@ApiModelProperty(notes = "Passwortzurücksetzung")
	@OneToOne(mappedBy="user")
	private ResetPassword resetPassword;

	@ApiModelProperty(notes = "Absender")
	@Transient
	private SseEmitter emitter;

	/**
	 * Instantiates a new user.
	 */
	public User() { super(); }
	
	/**
	 * Adds the push notification.
	 *
	 * @param pushNotification the push notification
	 * @return the push notification
	 */
	public DailyPushNotificationData addPushNotification(DailyPushNotificationData pushNotification) {
		getPushNotifications().add(pushNotification);
		pushNotification.setUser(this);

		return pushNotification;
	}

	/**
	 * Removes the push notification.
	 *
	 * @param pushNotification the push notification
	 * @return the push notification
	 */
	public DailyPushNotificationData removePushNotification(DailyPushNotificationData pushNotification) {
		getPushNotifications().remove(pushNotification);
		pushNotification.setUser(null);

		return pushNotification;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(getUserType().getName()));
		return authorities;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}