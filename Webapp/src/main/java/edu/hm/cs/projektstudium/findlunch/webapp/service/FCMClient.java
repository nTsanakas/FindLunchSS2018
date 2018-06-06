package edu.hm.cs.projektstudium.findlunch.webapp.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import edu.hm.cs.projektstudium.findlunch.webapp.controller.RestaurantController;
import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMClient {

    @Autowired
    private ResourceLoader resourceLoader;

    private final Logger LOGGER = LoggerFactory.getLogger(RestaurantController.class);

    public FCMClient() {
        Resource resource = resourceLoader.getResource("${push.fcm.adminkey}");
        try {
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(resource.getInputStream())).build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            LOGGER.error(LogUtils.getExceptionMessage("Constructor of FCMClient", e));

        }
    }

    public String sendDataMessage(String pushToken, Map<String, String> data)
            throws InterruptedException, ExecutionException {
        Message message = Message.builder().putAllData(data).setToken(pushToken)
                .build();

        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

}

