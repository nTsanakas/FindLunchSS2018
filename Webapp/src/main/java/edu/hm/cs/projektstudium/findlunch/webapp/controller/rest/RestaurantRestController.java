package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.view.RestaurantView;
import edu.hm.cs.projektstudium.findlunch.webapp.distance.DistanceCalculator;
import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.*;
import edu.hm.cs.projektstudium.findlunch.webapp.model.comparison.RestaurantDistanceComparator;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.PointsRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.RestaurantRepository;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class RestaurantRestController. The class is responsible for handling
 * rest calls related to registering users.
 */
@RestController
@Api(
		value="Restaurant",
		description="Registrierung von Nutzern in Restaurants.")
public class RestaurantRestController {

	/** The restaurant repository. */
	private final RestaurantRepository restaurantRepo;
	
	/** The points repository. */
	private final PointsRepository pointsRepository;

	/** The logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(RestaurantRestController.class);

	@Autowired
	public RestaurantRestController(RestaurantRepository restaurantRepo, PointsRepository pointsRepository) {
		this.restaurantRepo = restaurantRepo;
		this.pointsRepository = pointsRepository;
	}

	/**
	 * Gets the restaurants within a given radius with a flag showing if the
	 * restaurants is a favorite of the authenticated user. Ordered by distance
	 * (ascending)
	 *
	 * @param request the HttpServletRequest
	 * @param longitude
	 *            the longitude used to get the center for the radius
	 *            calculation
	 * @param latitude
	 *            the latitude used to get the center for the radius calculation
	 * @param radius
	 *            the radius within the restaurants should be located
	 * @param principal
	 *            the principal to get the authenticated user
	 * @return the restaurants within the given radius with a flag showing if
	 *         the restaurants is a favorite of the authenticated user
	 */
	@CrossOrigin
	@JsonView(RestaurantView.RestaurantRest.class)
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(
			value = "Restaurants in der Umgebung mit eigenen Favoriten abrufen.",
			response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Restaurants erfolgreich abgerufen."),
			@ApiResponse(code = 401, message = "Nicht autorisiert.")
	})
	@RequestMapping(
			path = "/api/restaurants",
			method = RequestMethod.GET,
			headers = { "Authorization" },
			produces = "application/json")
	public List<Restaurant> getRestaurantsAsAuthenticated(
			@RequestParam(name = "longitude")
            @ApiParam(
            		name = "Longitude",
					value = "Längengrad",
					required = true)
            float longitude,
			@RequestParam(name = "latitude")
			@ApiParam(
					name = "Latitude",
					value = "Breitengrad",
					required = true)
            float latitude,
			@RequestParam(name = "radius")
			@ApiParam(
					name = "Radius",
					value = "Bereich, um den herum gesucht werden soll.",
					required = true)
            int radius, Principal principal, HttpServletRequest request) {
		LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));
		
		User authenticatedUser = (User) ((Authentication) principal).getPrincipal();

		List<Restaurant> restaurantList = getAllRestaurants(longitude, latitude, radius);

		// Check favorites
		List<Restaurant> favorites = restaurantRepo.findByFavUsers_username(authenticatedUser.getUsername());

		for (Restaurant restaurant : restaurantList) {
			for (Restaurant favorite : favorites) {
				if (restaurant.getId() == favorite.getId()) {
					restaurant.setFavorite(true);
				}
			}
		}
		
		//add current points
		List<Points> pointsList = pointsRepository.findByCompositeKey_User_Id(authenticatedUser.getId());
		
		for (Restaurant restaurant : restaurantList) {
			for(Points points : pointsList){
				if(restaurant.getId() == points.getRestaurant().getId()){
					restaurant.setActuallPoints(points.getPoints());
				}
			}
		}
		
		return restaurantList;
	}

	/**
	 * Gets the restaurants within a given radius. The flag "isFavorite" is
	 * always false. Ordered by distance (ascending)
	 *
	 * @param request the HttpServletRequest
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
	@CrossOrigin
	@JsonView(RestaurantView.RestaurantRest.class)
	@ApiOperation(
			value = "Restaurants in der Umgebung abrufen.",
			response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Restaurants erfolgreich abgerufen.")
	})
	@RequestMapping(
			path = "/api/restaurants",
			method = RequestMethod.GET,
			produces = "application/json")
	public List<Restaurant> getRestaurants(
			@RequestParam(name = "longitude")
			@ApiParam(
					name = "Longitude",
					value = "Längengrad",
					required = true)
					float longitude,
			@RequestParam(name = "latitude")
			@ApiParam(
					name = "Latitude",
					value = "Breitengrad",
					required = true)
					float latitude,
			@RequestParam(name = "radius")
			@ApiParam(
					name = "Radius",
					value = "Bereich, um den herum gesucht werden soll.",
					required = true)
					int radius, HttpServletRequest request) {

		LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

		return getAllRestaurants(longitude, latitude, radius);
	}
	
	/**
	 * Gets the restaurants within a given radius. The flag "isFavorite" is
	 * always false. Ordered by distance (ascending)
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
	public List<Restaurant> getAllRestaurants(float longitude, float latitude, int radius)
	{
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

		// sort by distance (ascending)
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
	 * Exception handler for MethodArgumentTypeMismatchException.
	 *
	 * @param request the HttpServletRequest
	 * @param e
	 *            the exception
	 * @return the name of the exception class
	 */
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class })
	public String exceptionHandler(Exception e, HttpServletRequest request) {
		LOGGER.error(LogUtils.getExceptionMessage(request, Thread.currentThread().getStackTrace()[1].getMethodName(), e));
		return e.getClass().toString();
	}
}