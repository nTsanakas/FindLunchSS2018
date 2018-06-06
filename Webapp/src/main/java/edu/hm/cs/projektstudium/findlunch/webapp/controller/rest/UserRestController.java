package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


/**
 * Die Klasse UserRest Controller
 *
 * Wird von der Customer-App genutzt, um User-bezogene Operationen durchzuführen.
 * Augenblicklich ist nur die Änderung der Push-Subscription implementiert.
 */
@RestController
public class UserRestController {

    /** The user repository. */
    private final UserRepository userRepository;

    /** The logger. */
    private final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * REST-Endpunkt zum Aktivieren der Push-Notifikationen des Users.
     *
     * @param principal     Principal des eingeloggten Users.
     * @param request       HTTP-Request
     * @return              Response mit Statuscode.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(
            path = "/api/pushNotifications",
            method = RequestMethod.PUT)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Push-Notifikation erfolgreich aktiviert."),
            @ApiResponse(code = 401, message = "User nicht gefunden")})
    public ResponseEntity<String> activatePushNotification(
            HttpServletRequest request,
            Principal principal) {
        LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));
        return changePushNotification(principal, true);
    }

    /**
     * REST-Endpunkt zum Deativieren der Push-Notifikationen des Users.
     *
     * @param principal     Principal des eingeloggten Users.
     * @param request       HTTP-Request
     * @return              Response mit Statuscode.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(
            path = "/api/pushNotifications",
            method = RequestMethod.DELETE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Push-Notifikation erfolgreich deaktiviert."),
            @ApiResponse(code = 401, message = "User nicht gefunden")})
    public ResponseEntity<String> deactivatePushNotification(
            HttpServletRequest request,
            Principal principal) {
        LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));
        return changePushNotification(principal, false);
    }

    /**
     * Methode zum Ändern der Push-Benachrichtigungserlaubnis.
     *
     * @param principal Principal des eingeloggten Users
     * @param enabled aktivieren oder deaktivieren
     * @return ResponseEntity mit Ergebnis der Änderung
     */
    private ResponseEntity<String> changePushNotification(Principal principal, boolean enabled) {
        // User anhand ID finden.
        User user = userRepository.getOne(((User) ((Authentication) principal).getPrincipal()).getId());
        if(user!=null) {
            // Wenn User zur ID vorhanden ist, prüfen, ob ID der des anfragenden Users entspricht.
            if (user.getId()>0){
                user.setPushNotificationEnabled(enabled);
                userRepository.save(user);
                return new ResponseEntity<>("push-notifications enabled: " + user.isPushNotificationEnabled(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
