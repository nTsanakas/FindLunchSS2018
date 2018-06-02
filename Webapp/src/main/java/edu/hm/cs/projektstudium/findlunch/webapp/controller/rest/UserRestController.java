package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javafx.util.Pair;
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
     * @param patchValue    Key-Value-Paar mit Attributname und Parameter.
     * @param principal     Principal des eingeloggten Users.
     * @param request       HTTP-Request
     * @return              Response mit Statuscode.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(
            path = "/api/users/{id}",
            method = RequestMethod.PATCH,
            consumes = "application/JSON")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Parameter geändert"),
            @ApiResponse(code = 401, message = "User nicht gefunden"),
            @ApiResponse(code = 404, message = "Das Attribut wurde nicht gefunden oder kann nicht geändert werden"),
            @ApiResponse(code = 406, message = "Datentyp ist für Attribut ungültig")})
    public ResponseEntity<Integer> changeUserAttribute(
            @PathVariable("id") int id,
            @RequestBody Pair<String, Object> patchValue,
            HttpServletRequest request,
            Principal principal) {

        LOGGER.info(LogUtils.getInfoStringWithParameterList(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

        // User anhand ID finden.
        User user = userRepository.findOne(id);
        if(user != null) {
            // Wenn User zur ID vorhanden ist, prüfen, ob ID der des anfragenden Users entspricht.
            if (id == ((User) ((Authentication) principal).getPrincipal()).getId()){
                switch (patchValue.getKey()) {
                    // Key = PushNotificationEnabled
                    case "pushNotificationEnabled":
                        return changePushSubscription(user, patchValue.getValue());
                    // Platz für weitere Patch-Anwendungsfälle
                    // ...
                    default:
                        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(0, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Methode zum Ändern der Push-Benachrichtigungseinstellung.
     *
     * @param user                      User, der bearbeitet werden soll.
     * @param pushNotificationEnabled   Push-Benachrichtigung aktiviert/deaktiviert
     * @return                          ResponseEntity
     */
    private ResponseEntity<Integer> changePushSubscription(User user, Object pushNotificationEnabled){

        try {
            user.setPushNotificationEnabled((Boolean) pushNotificationEnabled);
            return new ResponseEntity<>(0, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(0, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
