package edu.hm.cs.projektstudium.findlunch.webapp.model;

import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonView;

import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.OfferView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.ReservationView;


/**
 * The Class Offer.
 */
@Entity
@ApiModel(
		description = "Beschreibt ein Essensangebot."
)
@Getter
@Setter
public class Offer {

	/** The id. */
	@ApiModelProperty(notes = "ID")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	private int id;

	/** The description. */
	@ApiModelProperty(notes = "Beschreibung")
	@Lob
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	@NotBlank(message="{offer.description.notBlank}")
	@Size(min=2, max=500, message= "{offer.description.lengthInvalid}")
	private String description;

	/** The preparation time. */
	@ApiModelProperty(notes = "Zubereitungszeit")
	@Column(name="preparation_time")
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	@Min(value=1, message="{offer.preparationTime.invalidMinValue}")
	private int preparationTime;

	/** The price. */
	@ApiModelProperty(notes = "Preis")
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	@NumberFormat(style=Style.DEFAULT)
	@DecimalMin(value="0.5", message="{offer.price.invalidMinValue}")
	private double price;

	/** The start date. */
	@ApiModelProperty(notes = "Beginn")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd.MM.yyy")
	@Column(name="start_date")
	@NotNull(message="{offer.startDate.notNull}")
	private Date startDate;

	/** The end date. */
	@ApiModelProperty(notes = "Angebotsende")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd.MM.yyy")
	@Column(name="end_date")
	@NotNull(message="{offer.endDate.notNull}")
	private Date endDate;

	/** The title. */
	@ApiModelProperty(notes = "Bezeichnung")
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	@NotBlank(message="{offer.title.notBlank}")
	@Size(min=2, max=60, message= "{offer.title.lengthInvalid}")
	private String title;

	/** The default photo. */
	@ApiModelProperty(notes = "Foto")
	@Transient
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	private OfferPhoto defaultPhoto;
	
	/** The needed point*/
	@ApiModelProperty(notes = "Anzahl benötigter Punkte")
	@JsonView({OfferView.OfferRest.class, ReservationView.ReservationRest.class})
	@Min(value=1, message="{offer.neededPoints.invalidMinValue}")
	private int neededPoints;
	
	/** The offers within the reservation */
	@ApiModelProperty(notes = "Angebote innerhalb der Bestellung")
	@OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
	private List<ReservationOffers> reservation_offers;

	/** The day of weeks. */
	//bi-directional many-to-many association to DayOfWeek
	@ApiModelProperty(notes = "Die Tage, für die das Angebot gilt.")
	@ManyToMany
	@JoinTable(
		name="offer_has_day_of_week"
		, joinColumns={
			@JoinColumn(name="offer_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="day_of_week_id")
			}
		)
	private List<DayOfWeek> dayOfWeeks;

	/** Additive. */
	//bi-directional many-to-many association to additives
	@ApiModelProperty(notes = "Eine Liste aus natürlichen oder künstlichen Additiven," +
			" die im Gericht enthalten sein könnten.")
	@JsonView(OfferView.OfferRest.class)
	@ManyToMany
	@JoinTable(
		name="offer_has_additives"
		, joinColumns={
			@JoinColumn(name="offer_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="additives_id")
			}
		)
	private List<Additive> additives;

	/** Allergenic. */
	//bi-directional many-to-many association to additives
	@ApiModelProperty(notes = "Eine Liste aus möglichen Allergenen, die im Gericht enthalten sein könnten.")
	@JsonView(OfferView.OfferRest.class)
	@ManyToMany
	@JoinTable(
		name="offer_has_allergenic"
		, joinColumns={
			@JoinColumn(name="offer_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="allergenic_id")
			}
		)
	private List<Allergenic> allergenic;

	/** The restaurant. */
	//bi-directional many-to-one association to Restaurant
	@ApiModelProperty(notes = "Das Restaurant, das das Angebot bereitstellt.")
    @JoinColumn(name="restaurant_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private Restaurant restaurant;
	
	/** The coursetype */
	@ApiModelProperty(notes = "Typ des Essenangebots.")
	@Column(name="course_type")
	private int courseType;
	
	/** The order of the offer within the coursetype */
	@ApiModelProperty(notes = "Hilfsattribut zur Sortierung.")
	@Column(name="sort")
	private int order;
	
	/** The offer photos. */
	//bi-directional many-to-one association to OfferPhoto
	@ApiModelProperty(notes = "Assoziation zu einem Foto für das Angebot.")
	@OneToMany(mappedBy="offer", cascade=CascadeType.ALL, orphanRemoval=true )
	private List<OfferPhoto> offerPhotos;

	/** Is sold out */
	@ApiModelProperty(notes = "Gibt an, ob der Angebotsvorrat ausverkauft ist.")
	@Column(name="sold_out")
	@JsonView(OfferView.OfferRest.class)
	private boolean sold_out;

	@ApiModelProperty(notes = "Die Id vom Änderungsantrag des Anbieters.")
	@Column(name = "swa_change_request_id")
	private int changeRequestId;

	@ApiModelProperty(notes = "Kommentar des Verrtriebsmitarbeiters bezüglich der letzten Änderung.")
	@Column(name = "swa_comment_of_last_change")
	private String commentOfLastChange;

	@ApiModelProperty(notes = "Der letzte Vertriebsmitarbeiter, der eine Änderung durchgeführt hat.")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "swa_last_changed_by_sales_person_id")
	private SalesPerson salesPerson;

	/**
	 * Instantiates a new offer.
	 */
	public Offer() {
		
		this.offerPhotos = new ArrayList<OfferPhoto>();
	}

	/**
	 * Adds the offer photo.
	 *
	 * @param offerPhoto the offer photo
	 * @return the offer photo
	 */
	public OfferPhoto addOfferPhoto(OfferPhoto offerPhoto) {
		getOfferPhotos().add(offerPhoto);
		offerPhoto.setOffer(this);

		return offerPhotos.get(offerPhotos.size() - 1);
	}

	/**
	 * Removes the offer photo.
	 *
	 * @param offerPhoto the offer photo
	 * @return the offer photo
	 */
	// TODO: Es hat keinen Sinn, das Argument zurückzugeben. Vorerst behalten, muss aber geändert werden.
	public OfferPhoto removeOfferPhoto(OfferPhoto offerPhoto) {
		getOfferPhotos().remove(offerPhoto);
		offerPhoto.setOffer(null);

		return offerPhoto;
	}
}