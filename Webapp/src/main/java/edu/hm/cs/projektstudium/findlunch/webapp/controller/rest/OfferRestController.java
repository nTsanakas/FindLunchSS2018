package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.OfferView;
import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.*;
import edu.hm.cs.projektstudium.findlunch.webapp.model.comparison.RestaurantDistanceComparator;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.CourseTypeRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.OfferRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.RestaurantRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.service.DistanceCalculator;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

/**
 * The Class OfferRestController. The class is responsible for handling rest
 * calls related to offers
 */
@RestController
@Api(
		value="Angebote",
		description="Zum Zugriff auf Angebote.")
public class OfferRestController {

	/** The offer repository. */
	private final OfferRepository offerRepo;

	/** The restaurant repository. */
	private final RestaurantRepository restaurantRepo;
	
	private final CourseTypeRepository courseTypeRepo;

	/** The logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(OfferRestController.class);

	@Autowired
	public OfferRestController(CourseTypeRepository courseTypeRepo, RestaurantRepository restaurantRepo, OfferRepository offerRepo) {
		this.courseTypeRepo = courseTypeRepo;
		this.restaurantRepo = restaurantRepo;
		this.offerRepo = offerRepo;
	}

	/**
	 * Gets the offers of a given restaurant.
	 *
	 * @param request the HttpServletRequest
	 * @param restaurantId
	 *            the id of the restaurant
	 * @return the offers of the given restaurant with the corresponding coursetype
	 */
	@CrossOrigin
	@JsonView(OfferView.OfferRest.class)
	@ApiOperation(
			value = "Angebote eines Restaurants abrufen.",
			response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Angebote erfolgreich abgerufen")
	})
	@RequestMapping(
			path = "/api/restaurants/{id}/offers",
			method = RequestMethod.GET,
			produces = "application/json")
	public Map<String, List<Offer>> getOffersToRestaurant(
			@PathVariable("id")
            @ApiParam(
				value = "ID des Restaurants",
				required = true)
            int restaurantId,
			HttpServletRequest request) {
		LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));
		
		List<Offer> result = new ArrayList<Offer>();
		Restaurant restaurant = restaurantRepo.findById(restaurantId);
		if (restaurant != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			// check if restaurant has a TimeSchedule for today
			TimeSchedule ts = restaurant.getTimeSchedules().stream()
					.filter(item -> item.getDayOfWeek().getDayNumber() == c.get(Calendar.DAY_OF_WEEK)).findFirst()
					.orElse(null);

			// only get offers, that are valid at the moment
			if (ts != null) {
				getValidOffers(c, ts, restaurantId, result);
			}
		}
		
		Map<String, List<Offer>> offers = new LinkedHashMap<String, List<Offer>>();
		
		// Put the orders together with the coursetypes and order them by their sort by value
		for(CourseTypes course : courseTypeRepo.findByRestaurantIdOrderBySortByAsc(restaurantId)){
			
			List<Offer> offersInCourse = new LinkedList<Offer>();
			for(Offer offer  : result){
				if(offer.getCourseType() == course.getId() && !offer.getSold_out()){
					offersInCourse.add(offer);
				}
			}
			if(!offersInCourse.isEmpty()){
				offers.put(course.getName(), offersInCourse);
			}
		}
		return offers;
	}

	/**
	 * 10 Angebote in der Nähe des Aufenthaltsorts abrufen.
	 *
	 * Die Methode
	 *
	 * @param request Der HttpServletRequest
	 * @param latitude Breitengrad
	 * @param longitude	Längengrad
	 * @param principal Principal des Nutzers
	 * @return 10 Angebote im Umkreis des aktuellen Aufenhaltsortes
	 */
	@CrossOrigin
	@JsonView(OfferView.OfferRest.class)
	//@PreAuthorize("isAuthenticated()")
	@ApiOperation(
			value = "Angebote rund um eine bestimmte Position abrufen",
			response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Angebote erfolgreich abgerufen")
	})
	@RequestMapping(
			path = "/api/offers",
			method = RequestMethod.GET,
			produces = "application/json")
	public Map<String, List<Offer>> getLocationBasedOffers(
			@RequestParam
			@ApiParam(
					value = "Längengrad",
					required = true)
					float longitude,
			@RequestParam
			@ApiParam(
					value = "Breitengrad",
					required = true)
					float latitude,
			HttpServletRequest request/*,
			Principal principal*/) {
		LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

		Calendar c = Calendar.getInstance();
		c.setTime((new Date()));

		return getOffersToLocation(longitude, latitude, 10,
				c/*, ((User) ((Authentication) principal).getPrincipal()).getUsername()*/);

	}

	private Map<String,List<Offer>> getOffersToLocation(float longitude, float latitude, int offerAmount, Calendar c/*, String username*/) {

		int radius = 2000;
		List<Offer> offers = new ArrayList<Offer>();
		List<Restaurant> restaurants;

		restaurants = getAllRestaurants(longitude, latitude, radius);
		Map<String, List<Offer>> offersToRestaurant = new LinkedHashMap<String, List<Offer>>();
		for (Restaurant restaurant:restaurants) {
			if (restaurant != null) {
				// check if restaurant has a TimeSchedule for today
				TimeSchedule ts = restaurant.getTimeSchedules().stream().filter(item -> item.getDayOfWeek().getDayNumber() == c.get(Calendar.DAY_OF_WEEK)).findFirst().orElse(null);

				// only get , that are valid at the moment
				if (ts != null) {
					getValidOffers(c, ts, restaurant.getId(), offers);
					offersToRestaurant.put(restaurant.getName(), offers);
				}
			}
		}


		return offersToRestaurant;

		/*TODO: 1. Entweder eine SQL-Abfrage schreiben, die eine Anzahl an Angeboten von Restaurants ausgibt, die gerade offen haben und im Umkreis liegen.
				2. Oder vorhandene tolle Abfragen nutzen und murksen.
				1. Alle Restaurants im Radius suchen.
				2. Dazu alle aktuellen Angebote per for-Schleife raussuchen lassen.
				3. Nur eins nehen je Restaurant.

		*/

		/**
		 * Rückgabe:
		 * Nur von aktuell/zur angegebenen Uhrzeit geöffneten Restaurants
		 * Art des Gangs
		 * Entfernung
		 * Angebotsobjekt
		 * Optional: Favorit ja/nein
		 *
		 *
		 */
	}


	/**
	 * Gets the restaurants within a given radius. The flag "isFavorite" is
	 * always false. Ordered by service (ascending)
	 *
	 * @param longitude
	 *            the longitude used to get the center for the radius
	 *            calculation
	 * @param latitude
	 *            the latitude used to get the center for the radius calculation
	 * @param radius
	 *            the radius within the restaurants should be located
	 * @return the restaurants within the given radius. The flag "isFavorite" is
	 *         always false
	 */
	private List<Restaurant> getAllRestaurants (float longitude, float latitude, int radius){
		List<Restaurant> restaurantList = restaurantRepo.findAll();

		for (int i = 0; i < restaurantList.size(); i++) {
			Restaurant currentRestaurant = restaurantList.get(i);
			currentRestaurant.setDistance(DistanceCalculator.calculateDistance(latitude, longitude,
					currentRestaurant.getLocationLatitude(), currentRestaurant.getLocationLongitude()));

			// remove restaurants which are located outside the radius around
			// the user location.
			if (currentRestaurant.getDistance() > radius) {
				restaurantList.remove(i);
				i--;
			}
		}

		// sort by service (ascending)
		restaurantList.sort(new RestaurantDistanceComparator());

		// If the restaurant has no specific openingtimes it has to be set on the offertime
		for(Restaurant restaurant : restaurantList) {
			for(TimeSchedule schedule : restaurant.getTimeSchedules()) {
				if(schedule.getOpeningTimes().isEmpty()) {
					List<OpeningTime> openingTimes = new ArrayList<>();
					OpeningTime open = new OpeningTime();

					open.setTimeSchedule(schedule);
					open.setOpeningTime(schedule.getOfferStartTime());
					open.setClosingTime(schedule.getOfferEndTime());
					openingTimes.add(open);
					schedule.setOpeningTimes(openingTimes);
				}
			}
		}
		return restaurantList;
	}

	/**
	 * Gets the valid offers of a restaurant.
	 *
	 * @param c
	 *            the Calendar with the day and time to check (preferred: now)
	 * @param ts
	 *            the TimeSchedule that has to be checked
	 * @param restaurantId
	 *            the id of the Restaurant
	 * @param result
	 *            the result, where the valid offers should be stored
	 */
	private void getValidOffers(Calendar c, TimeSchedule ts, int restaurantId, List<Offer> result) {

		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMin = c.get(Calendar.MINUTE);
		int currentTime = currentHour * 60 + currentMin;

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(ts.getOfferStartTime());
		int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
		int startMin = startCalendar.get(Calendar.MINUTE);
		int startTime = startHour * 60 + startMin;

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(ts.getOfferEndTime());
		int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
		int endMin = endCalendar.get(Calendar.MINUTE);
		int endTime = endHour * 60 + endMin;

		if (startTime <= currentTime && endTime >= currentTime) {
			Date today = getZeroTimeDate(new Date());

			for (Offer o : offerRepo.findByRestaurant_idOrderByOrderAsc(restaurantId)) {
				Date startDate = getZeroTimeDate(o.getStartDate());
				Date endDate = getZeroTimeDate(o.getEndDate());

				if (today.equals(startDate) || today.equals(endDate)
						|| (today.after(startDate) && today.before(endDate))) {

					if (o.getDayOfWeeks().stream().filter(item -> item.getDayNumber() == c.get(Calendar.DAY_OF_WEEK))
							.findFirst().orElse(null) != null) {
						result.add(o);
					}
				}
			}
		}
	}

	/**
	 * Exception handler for MethodArgumentTypeMismatchException.
	 *
	 * @param request the HttpServletRequest
	 * @param e
	 *            the exception
	 * @return the name of the exception class
	 */
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public String exceptionHandler(Exception e, HttpServletRequest request) {
		LOGGER.error(LogUtils.getExceptionMessage(request, Thread.currentThread().getStackTrace()[1].getMethodName(), e));
		return e.getClass().toString();
	}

	/**
	 * Removes the time (hour, minute, second, millisecond) of a given date and
	 * returns the time value
	 *
	 * @param date
	 *            the date, where the time should be set to zero
	 * @return the time value of the date, where the time was set to zero
	 */
	private static Date getZeroTimeDate(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
}