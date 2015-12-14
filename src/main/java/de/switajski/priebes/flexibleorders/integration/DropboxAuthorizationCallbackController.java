package de.switajski.priebes.flexibleorders.integration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;

import de.switajski.priebes.flexibleorders.web.ExceptionController;

@Controller
public class DropboxAuthorizationCallbackController extends ExceptionController {

    private static Logger log = Logger.getLogger(DropboxAuthorizationCallbackController.class);

    @Autowired
    DropboxClientWrapper accessTokenHolder;

    @RequestMapping(value = "/dropbox-auth-finish", method = RequestMethod.GET, params = { "state", "code" })
    public String sendReport(
            @RequestParam("state") String state,
            @RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String successSite = "resources/dropbox-auth-success.html";
        String refusedSite = "resources/dropbox-auth-refused.html";

        try {
            accessTokenHolder.finish(request.getParameterMap());
        }
        catch (DbxWebAuth.BadRequestException ex) {
            log.error("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
            response.sendError(400);
            return successSite;
        }
        catch (DbxWebAuth.BadStateException ex) {
            // Send them back to the start of the auth flow.
            // TODO: make send JsonObjectResponse
            // response.sendRedirect("http://my-server.com/dropbox-auth-start");
            return successSite;
        }
        catch (DbxWebAuth.CsrfException ex) {
            log.error("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
            return successSite;
        }
        catch (DbxWebAuth.NotApprovedException ex) {
            // When Dropbox asked "Do you want to allow this app to access your
            // Dropbox account?", the user clicked "No".
            return refusedSite;
        }
        catch (DbxWebAuth.ProviderException ex) {
            log.error("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
            return successSite;
        }
        catch (DbxException ex) {
            log.error("On /dropbox-auth-finish: Error getting token: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
            return successSite;
        }

        return successSite;
    }

}
