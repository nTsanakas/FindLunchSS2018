package edu.hm.cs.projektstudium.findlunch.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;


/**
 * The Class KitchenType. Defines different kitchen types.
 */
@Entity
@Table(name="kitchen_type")
@ApiModel(
		description = "Definiert verschiedene Küchentypen."
)
public class KitchenType {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(RestaurantView.RestaurantRest.class)
	private int id;

	/** The name. */
	@ApiModelProperty(notes = "Name")
	@JsonView(RestaurantView.RestaurantRest.class)
	private String name;


	/** The restaurants. */
	@ApiModelProperty(notes = "Restaurants")
	//bi-directional many-to-many association to Restaurant
	@ManyToMany(mappedBy="kitchenTypes")
	@JsonIgnore
	private List<Restaurant> restaurants;

	/**
	 * Instantiates a new kitchen type.
	 */
	public KitchenType() {
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

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
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
	 * Gets the restaurants.
	 *
	 * @return the restaurants
	 */
	public List<Restaurant> getRestaurants() {
		return this.restaurants;
	}

	/**
	 * Sets the restaurants.
	 *
	 * @param restaurants the new restaurants
	 */
	public void setRestaurants(List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

}