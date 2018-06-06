package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class User. A user of the FindLunch system. Sets information about a user.
 */
@Entity
// Inherited property needs to be ignored or else the RegisterUserRestController integration test is not working when passing a user object to the request.
@JsonIgnoreProperties({"authorities"})
@ApiModel(
		description = "Ein Benutzer des FindLunch-Systems."
)
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

	@ApiModelProperty(notes = "Push-Benachrichtigung eingeschaltet")
	private boolean pushNotificationEnabled;

	public SseEmitter getEmitter() {
		return emitter;
	}

	public void setEmitter(SseEmitter emitter) {
		this.emitter = emitter;
	}

	/**
	 * Instantiates a new user.
	 */
	public User() {
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the passwordconfirm.
	 *
	 * @return the passwordconfirm
	 */
	public String getPasswordconfirm() {
		return this.passwordconfirm;
	}

	/**
	 * Sets the passwordconfirm.
	 *
	 * @param passwordconfirm the new passwordconfirm
	 */
	public void setPasswordconfirm(String passwordconfirm) {
		this.passwordconfirm = passwordconfirm;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the favorites.
	 *
	 * @return the favorites
	 */
	public List<Restaurant> getFavorites() {
		return this.favorites;
	}

	/**
	 * Sets the favorites.
	 *
	 * @param restaurants the new favorites
	 */
	public void setFavorites(List<Restaurant> restaurants) {
		this.favorites = restaurants;
	}

	/**
	 * Gets the administrated restaurant.
	 *
	 * @return the restaurant
	 */
	public Restaurant getAdministratedRestaurant() {
		return this.restaurant;
	}

	/**
	 * Sets the administrated restaurant.
	 *
	 * @param restaurant the new restaurant
	 */
	public void setAdministratedRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	/**
	 * Gets the user type.
	 *
	 * @return the user type
	 */
	public UserType getUserType() {
		return this.userType;
	}

	/**
	 * Sets the user type.
	 *
	 * @param userType the new user type
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
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
	/**
	 * Makes sure the account is not expired.
	 * @return true
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	/**
	 * Makes sure the account is not locked.
	 * @return true
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	/**
	 * Makes sure the credentials are not expired.
	 * @return true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	/**
	 * Makes sure the account is enabled.
	 * @return true
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	/**
	 * Gets the points of the user.
	 * @return The points of the user
	 */
	public List<Points> getAllPoints(){
		return userPoints;
	}
	
	/**
	 * Sets the points of the user.
	 * @param userPoints List of Points to set
	 */
	public void setAllPoints(List<Points> userPoints){
		this.userPoints = userPoints;
	}
	
	/**
	 * Add Points to an User.
	 * @param points Points to add
	 */
	public void addPoints(Points points){
		this.userPoints.add(points);
	}

	/**
	 * Gets the account of the user.
	 * @return The account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets the account of the user.
	 * @param account The new account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	/**
	 * Gets the Captcha object.
	 *
	 * @return the Captcha object
	 */
	public Captcha getCaptcha() {
		return captcha;
	}
	
	//public void setFcmId(String fcmId){
	//	this.fcmId = fcmId;
	//}
	
	//public String getFcmId(){
	//	return fcmId;
	//}

	/**
	 * Initiates the password reset.
	 */
	public ResetPassword getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
	}

	public boolean isPushNotificationEnabled() {
		return pushNotificationEnabled;
	}

	public void setPushNotificationEnabled(boolean pushNotificationEnabled) {
		this.pushNotificationEnabled = pushNotificationEnabled;
	}

}