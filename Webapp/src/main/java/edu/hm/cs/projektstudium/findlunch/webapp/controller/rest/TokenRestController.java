package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import javax.servlet.http.HttpServletRequest;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.PushToken;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.PushTokenRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.UserRepository;

/**
 * The Class TokenRestController.
 * The class is responsible for handling API calls to store the customers Firebase Token into the database.
 * 
 * @author Niklas Klotz
 * @edited Paul Mandl
 *
 */
@RestController
@Api(value="Firebase-Token", description="Verwaltung des Firebase-Tokens.")
public class TokenRestController {

	final UserRepository userRepository;

	final PushTokenRepository pushTokenRepository;

	/**
	 * The logger.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(LogRestController.class);

	@Autowired
	public TokenRestController(UserRepository userRepository, PushTokenRepository pushTokenRepository) {
		this.userRepository = userRepository;
		this.pushTokenRepository = pushTokenRepository;
	}

	/**
	 * Puts the token into the database
	 *
	 * @param pushToken the customers token
	 * @param user      the user
	 * @param request   the http request
	 * @return response entity representing a status code
	 */
	@CrossOrigin
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "Firebase-Token in Datenbank schreiben.", response = Integer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Token erfolgreich aktualisiert."),
			@ApiResponse(code = 202, message = "Neuer Token erfolgreich gesendet."),
			@ApiResponse(code = 208, message = "Token ist bereits vorhanden."),
			@ApiResponse(code = 401, message = "Nicht autorisiert.")})
	@RequestMapping(path = "api/submitToken/{pushToken}", method = RequestMethod.PUT, produces = "application/json")
	ResponseEntity<Integer> submitToken(
			@PathVariable
			@ApiParam(value = "Firebase-Token, der für den Push genutzt werden soll.", required = true)
					String pushToken,
			@RequestBody User user,
			HttpServletRequest request) {

		LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

        User authenticatedUser = userRepository.findByUsername(user.getUsername());

        PushToken oldToken = pushTokenRepository.findByUserId(authenticatedUser.getId());

        // if there is no token stored for the customer yet
        if(oldToken==null){
            PushToken newToken = new PushToken();
            newToken.setUser_id(authenticatedUser.getId());
            newToken.setFcm_token(pushToken);
            pushTokenRepository.save(newToken);
            return new ResponseEntity<>(0, HttpStatus.ACCEPTED);
        }
        // refresh the customers token
        else if(!oldToken.getFcm_token().equals(pushToken)) {
            pushTokenRepository.delete(oldToken.getId());
            PushToken newToken = new PushToken();
            newToken.setUser_id(authenticatedUser.getId());
            newToken.setFcm_token(pushToken);
            pushTokenRepository.save(newToken);
            return new ResponseEntity<>(1, HttpStatus.OK);
        }
        // if the customer token is already in the database
        else {
            return new ResponseEntity<>(2, HttpStatus.ALREADY_REPORTED);
        }
    }

}