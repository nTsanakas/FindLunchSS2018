package edu.hm.cs.projektstudium.findlunch.webapp.controller.rest;

import edu.hm.cs.projektstudium.findlunch.webapp.logging.LogUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * This class could be used for a generic Captcha handling process.
 * For example Captchas or responses for Captchas from different providers could be obtained through the methods of
 * this class.
 */
@RestController
@Api(
        value="Captcha-Behalung",
        description="Kann für Captcha-Handling-Prozess verwendet werden.")
public class GenericCaptchaRestController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GenericCaptchaRestController.class);

    /**
     * Gets the Captcha or a Captcha-response of the requested Captcha provider.
     *
     * @param request the HttpServletRequest
     * @return the log file.
     */
    @CrossOrigin
    @ApiOperation(
            value = "Abruf eines Captcha vom gewünschten Provider.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Captcha erfolgreich abgerufen."),
            @ApiResponse(code = 400, message = "Fehler beim Abruf.")
    })
    @RequestMapping(
            path = "/api/captcha",
            method = RequestMethod.GET,
            params = {"provider"},
            produces = "text/html")
    public final ResponseEntity<String> getRemoteContent(final HttpServletRequest request) throws IOException {
        LOGGER.info(LogUtils.getDefaultInfoString(request, Thread.currentThread().getStackTrace()[1].getMethodName()));

        Response response = null;
        try {
            response = new OkHttpClient().newCall(new Request.Builder()
                    .url(request.getParameterValues("provider")[0])
                    .get()
                    .build()).execute();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(response != null ? response.body().string() : "No body", HttpStatus.OK);
    }
}
