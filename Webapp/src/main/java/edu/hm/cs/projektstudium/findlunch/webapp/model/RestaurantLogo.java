package edu.hm.cs.projektstudium.findlunch.webapp.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.OfferView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The Class RestaurantLogo.
 * @author Niklas Klotz
 *
 */
@Entity
@Table(name="restaurant_logo")
@ApiModel(
		description = "Logo zu einem Restaurant."
)
public class RestaurantLogo {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@JsonView(RestaurantView.RestaurantRest.class)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** The logo. */
	@ApiModelProperty(notes = "Logo")
	@Lob
	@JsonView(RestaurantView.RestaurantRest.class)
	private byte[] logo;
	
	/** The offer. */
	@ApiModelProperty(notes = "Angebot")
	//bi-directional many-to-one association to Offer
	@ManyToOne(fetch=FetchType.LAZY)
	private Restaurant restaurant;
	
	/** The thumbnail. */
	@ApiModelProperty(notes = "Thumbnail")
	@Lob
	@JsonView(RestaurantView.RestaurantRest.class)
	private byte[] thumbnail;
	
	/** The base 64 encoded. */
	@ApiModelProperty(notes = "Logo base64-kodiert")
	@Transient
	@JsonIgnore
	private String base64Encoded;
	
	/** The image format. */
	@ApiModelProperty(notes = "Bildformat")
	@Transient
	@JsonIgnore
	private String imageFormat;
	
	/**
	 * Instantiates a new Restaurant Logo.
	 */
	public RestaurantLogo() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getBase64Encoded() {
		return base64Encoded;
	}

	public void setBase64Encoded(String base64Encoded) {
		this.base64Encoded = base64Encoded;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
