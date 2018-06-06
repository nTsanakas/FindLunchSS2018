package edu.hm.cs.projektstudium.findlunch.webapp.service;

import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import edu.hm.cs.projektstudium.findlunch.webapp.model.PushToken;
import edu.hm.cs.projektstudium.findlunch.webapp.model.User;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.PushTokenRepository;
import edu.hm.cs.projektstudium.findlunch.webapp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMPushService {

    /**
     * The logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(FCMPushService.class);
    /**
     * Identification string in database for not valid token.
     * Other service will be used and processed.
     */
    private final static String NOT_AVAILABLE = "notAvailable";

    /**
     * FCM-Client zur Initialisierung der Verbindung zu Firebase.
     */
    private final FCMClient fcmClient;

    /**
     * User-Repository
     */
    private final UserRepository userRepository;

    /**
     * Push-Token-Repository mit Token zur User-Id.
     */
    private final PushTokenRepository pushTokenRepository;

    @Autowired
    public FCMPushService(FCMClient fcmClient, UserRepository userRepository, PushTokenRepository pushTokenRepository) {
        this.fcmClient = fcmClient;
        this.userRepository = userRepository;
        this.pushTokenRepository = pushTokenRepository;
    }

    @Scheduled(cron = "${push.scheduler.daily.cron}", zone = "${push.scheduler.daily.zone}")
    void sendDailyPushNotifications() {

        LOGGER.info(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Beginne mit Versand der täglichen Push-Nachrichten."));

        // User mit gewünschter Push-Notifikation ermitteln.
        List<User> users = userRepository.findAllByPushNotificationEnabledIsTrue();
        for(User user : users){
            // Push-Token-Objekt zu jedem Nutzer suchen.
            PushToken pushToken = pushTokenRepository.findByUserId(user.getId());
            // Wenn ein dazugehöriger Push-Token, der gültig ist, gefunden wurde, wird die Push-Nachricht erstellt und
            // versendet.
            if(pushToken!=null){
                if(!pushToken.getFcm_token().equals(NOT_AVAILABLE)) {
                    // "data" : - Werte für Nachricht in HashMap ablegen.
                    Map<String, String> data = new HashMap<>();
                    data.put("command", "SEND_LOCATION");
                    try {
                        // Nachricht als Data-Message senden.
                        String result = this.fcmClient.sendDataMessage(pushToken.getFcm_token(), data);
                        LOGGER.info(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(), result));
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(),
                                e.getMessage()));
                    }
                } else {
                    LOGGER.error(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(),
                            "Push-Token für User mit ID " + user.getId() + " nicht verfügbar, obwohl er Push-Nachrichten erhalten will."));
                }
            } else {
                LOGGER.error(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        "Push-Token für User mit ID " + user.getId() + " nicht existent, obwohl er Push-Nachrichten erhalten will."));
            }
        }
        LOGGER.info(LogUtils.getDefaultSchedulerMessage(Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Nachrichtenversand für " + users.size() + " User abgeschlossen."));
    }



}
