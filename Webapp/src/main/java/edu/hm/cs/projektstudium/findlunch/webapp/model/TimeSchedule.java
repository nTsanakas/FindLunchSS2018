package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;

/**
 * The Class TimeSchedule.
 */
@Entity
@Table(name = "time_schedule")
@ApiModel(
		description = "Definiert einen Zeitraum, innerhalb dessen ein Angebot gültig ist."
		)
@Getter
@Setter
public class TimeSchedule {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/** The offer end time. */
	@ApiModelProperty(notes = "Ende der Angebotszeit")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de", timezone = "Europe/Berlin")
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "offer_end_time")
	@JsonView(RestaurantView.RestaurantRest.class)
	private Date offerEndTime;

	/** The offer start time. */
	@ApiModelProperty(notes = "Start der Angebotszeit")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de", timezone = "Europe/Berlin")
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "offer_start_time")
	@JsonView(RestaurantView.RestaurantRest.class)
	private Date offerStartTime;

	/** The opening times. */
	@ApiModelProperty(notes = "Öffnungszeiten")
	// bi-directional many-to-one association to OpeningTime
	@JsonView(RestaurantView.RestaurantRest.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "timeSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OpeningTime> openingTimes;

	/** The day of week. */
	@ApiModelProperty(notes = "Wochentag")
	// bi-directional many-to-one association to DayOfWeek
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "day_of_week_id")
	@JsonView(RestaurantView.RestaurantRest.class)
	private DayOfWeek dayOfWeek;

	/** The restaurant. */
	@ApiModelProperty(notes = "Restaurant")
	// bi-directional many-to-one association to Restaurant
	@ManyToOne(fetch = FetchType.EAGER)
	private Restaurant restaurant;

	/**
	 * Instantiates a new time schedule.
	 */
	public TimeSchedule() { super(); }

	public void setOpeningTimes(List<OpeningTime> openingTimes) {
		if(this.openingTimes == null)
			this.openingTimes = openingTimes;
		else
		{
			this.openingTimes.clear();
			if(openingTimes != null)
				this.openingTimes.addAll(openingTimes);
		}
	}

	/**
	 * Adds the opening time.
	 *
	 * @param openingTime the opening time
	 * @return the opening time
	 */
	public OpeningTime addOpeningTime(OpeningTime openingTime) {
		getOpeningTimes().add(openingTime);
		openingTime.setTimeSchedule(this);

		return openingTime;
	}

	/**
	 * Removes the opening time.
	 *
	 * @param openingTime the opening time
	 * @return the opening time
	 */
	public OpeningTime removeOpeningTime(OpeningTime openingTime) {
		getOpeningTimes().remove(openingTime);
		openingTime.setTimeSchedule(null);

		return openingTime;
	}

}