package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserRepository userRepository;

    /** The logger. */
    private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationRestController.class);

    /**
     * REST-Endpunkt zum Ändern eines Attributs des Users.
     *
     * @param id            Id des Users, bei dem ein Attribut geändert werden soll.
     * @param enabled       Push-Nachrichten aktiviert oder deaktiviert.
     * @param principal     Principal des eingeloggten Users.
     * @param request       HTTP-Request
     * @return              Response mit Statuscode.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(
            path = "/api/users/{id}/pushNotifications",
            method = RequestMethod.PATCH)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Push-Notifikation erfolgreich aktiviert/deaktiviert."),
            @ApiResponse(code = 401, message = "User nicht gefunden")})
    public ResponseEntity<String> changePushSubscription(
            @PathVariable int id,
            @RequestParam(name = "enabled")
            @ApiParam(
                    name = "enabled",
                    value = "aktiviert",
                    required = true)
                    Boolean enabled,
            HttpServletRequest request,
            Principal principal) {

        LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

        // User anhand ID finden.
        User user = userRepository.findOne(id);
        if(user != null) {
            // Wenn User zur ID vorhanden ist, prüfen, ob ID der des anfragenden Users entspricht.
            if (id == ((User) ((Authentication) principal).getPrincipal()).getId()){
                user.setPushNotificationEnabled(enabled);
                userRepository.save(user);
                return new ResponseEntity<>("push-notifications enabled: " + enabled, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
