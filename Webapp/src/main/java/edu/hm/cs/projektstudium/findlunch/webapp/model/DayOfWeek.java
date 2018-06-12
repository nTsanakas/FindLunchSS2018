package edu.hm.cs.projektstudium.findlunch.webapp.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;


/**
 * The Class DayOfWeek.
 */
@Entity
@Table(name="day_of_week")
@ApiModel(
		description = "Klasse für Wochentag."
)
public class DayOfWeek {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(RestaurantView.RestaurantRest.class)
	private int id;

	/** The day number. */
	@ApiModelProperty(notes = "Wochentag-Nummer")
	@Column(name="day_number")
	@JsonView(RestaurantView.RestaurantRest.class)
	private int dayNumber;

	/** The name. */
	@ApiModelProperty(notes = "Name des Wochentags")
	@JsonView(RestaurantView.RestaurantRest.class)
	private String name;

	/** The offers. */
	@ApiModelProperty(notes = "Angebote am Wochentag")
	//bi-directional many-to-many association to Offer
	@ManyToMany(mappedBy="dayOfWeeks")
	private List<Offer> offers;

	/** The time schedules. */
	@ApiModelProperty(notes = "Zeitpläne")
	//bi-directional many-to-one association to TimeSchedule
	@OneToMany(mappedBy="dayOfWeek")
	private List<TimeSchedule> timeSchedules;

	/**
	 * Instantiates a new day of week.
	 */
	public DayOfWeek() {
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
	 * Gets the day number.
	 *
	 * @return the day number
	 */
	public int getDayNumber() {
		return this.dayNumber;
	}

	/**
	 * Sets the day number.
	 *
	 * @param dayNumber the new day number
	 */
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
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
	 * Gets the offers.
	 *
	 * @return the offers
	 */
	public List<Offer> getOffers() {
		return this.offers;
	}

	/**
	 * Sets the offers.
	 *
	 * @param offers the new offers
	 */
	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	/**
	 * Gets the time schedules.
	 *
	 * @return the time schedules
	 */
	public List<TimeSchedule> getTimeSchedules() {
		return this.timeSchedules;
	}

	/**
	 * Sets the time schedules.
	 *
	 * @param timeSchedules the new time schedules
	 */
	public void setTimeSchedules(List<TimeSchedule> timeSchedules) {
		this.timeSchedules = timeSchedules;
	}

	/**
	 * Adds the time schedule.
	 *
	 * @param timeSchedule the time schedule
	 * @return the time schedule
	 */
	public TimeSchedule addTimeSchedule(TimeSchedule timeSchedule) {
		getTimeSchedules().add(timeSchedule);
		timeSchedule.setDayOfWeek(this);

		return timeSchedule;
	}

	/**
	 * Removes the time schedule.
	 *
	 * @param timeSchedule the time schedule
	 * @return the time schedule
	 */
	public TimeSchedule removeTimeSchedule(TimeSchedule timeSchedule) {
		getTimeSchedules().remove(timeSchedule);
		timeSchedule.setDayOfWeek(null);

		return timeSchedule;
	}
}