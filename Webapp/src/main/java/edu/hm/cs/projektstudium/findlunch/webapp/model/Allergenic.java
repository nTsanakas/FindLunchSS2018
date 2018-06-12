package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.OfferView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Describes the allergenes that could be contained in a product.
 */
@Entity
@Table(name="allergenic")
@ApiModel (
        description = "Beschreibt ein Allergen, das in Produkten enthalten sein kann."
)
public class Allergenic {

	/** The id. */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes = "ID")
	@JsonView({RestaurantView.RestaurantRest.class, OfferView.OfferRest.class})
	private int id;

	/** The name. */
	@Column(name="name")
	@ApiModelProperty(notes = "Name")
	@JsonView({RestaurantView.RestaurantRest.class, OfferView.OfferRest.class})
	private String name;

	/** The description. */
	@Column(name="description")
	@ApiModelProperty(notes = "Beschreibung")
	@JsonView({RestaurantView.RestaurantRest.class, OfferView.OfferRest.class})
	private String description;

	/** The short Key. */
	@Column(name="short")
	@ApiModelProperty(notes = "Kurzbezeichnung")
	@JsonView({RestaurantView.RestaurantRest.class, OfferView.OfferRest.class})
	private String shortKey;

	/** The offers. */
	@ApiModelProperty(notes = "Zugehörige Angebote")
	//bi-directional many-to-many association to Offer
	@ManyToMany(mappedBy="allergenic")
	private List<Offer> offers;
	
	/**
	 * Instantiates a new allergenic.
	 */
	public Allergenic() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortKey() {
		return shortKey;
	}

	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

}