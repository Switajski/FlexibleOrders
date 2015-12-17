package de.switajski.priebes.flexibleorders.integration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;

public class DropboxAuthorizationInterceptor implements HandlerInterceptor {

    private static Logger log = Logger.getLogger(DropboxAuthorizationInterceptor.class);

    @Autowired
    DropboxClientWrapper dropboxClientWrapper;

    // TODO: get it dynamically from web config
    private static final String APP_NAME = "FlexibleOrders";

    // TODO: redundant code
    private static final String REDIRECT_MAPPING = "dropbox-auth-finish";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!dropboxClientWrapper.hasAccess()) {
            String callbackUrl = callbackUrl(request);
            String userLocale = Locale.getDefault().toString();
            HttpSession session = request.getSession(true);

            String authorizePageUrl = dropboxClientWrapper.startAuthorization(callbackUrl, userLocale, session);

            redirect(response, authorizePageUrl, request.getHeader("origin"));
            return false;
        }
        log.error("DropboxAuthorizationInterceptor Prehandle");
        return true;
    }

    private String callbackUrl(HttpServletRequest request) throws MalformedURLException {
        return String.format("%s://%s:%d/" + APP_NAME + "/" + REDIRECT_MAPPING,
                request.getScheme(), request.getServerName(), request.getServerPort());
    }

    /**
     * Redirect the user to the Dropbox website so they can approve our
     * application. The Dropbox website will send them back to
     * "http://my-server.com/dropbox-auth-finish" when they're done.
     * 
     * could be a redirect like 'response.sendRedirect(authorizePageUrl);' - but
     * not with cors :( instead of redirect send success wirh redirect url
     * 
     * @param response
     * @param authorizePageUrl
     * @throws IOException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     */
    private void redirect(HttpServletResponse response, String authorizePageUrl, String origin)
            throws IOException,
            JsonGenerationException,
            JsonMappingException {

        PrintWriter out = response.getWriter();
        JsonObjectResponse jsonResponse = new JsonObjectResponse();
        jsonResponse.setSuccess(true);
        jsonResponse.setData(authorizePageUrl);

        addCorsHeaders(response, origin);
        response.setStatus(210);

        ObjectMapper mapper = new ObjectMapper();

        // read from file, convert it to user class
        mapper.writeValue(out, jsonResponse);
    }

    private void addCorsHeaders(HttpServletResponse response, String origin) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "accept, x-requested-with");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Max-Age", "1800");
        response.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
