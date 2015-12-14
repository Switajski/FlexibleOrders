package de.switajski.priebes.flexibleorders.integration;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DropboxClientWrapper {

    @Autowired
    DropboxConfiguration config;

    private DbxWebAuth auth;

    private String accessToken;

    private DbxClient dbxClient;

    public String startAuthorization(String callbackUrl, String userLocale, HttpSession session) {
        DbxRequestConfig requestConfig = new DbxRequestConfig(config.getClientId(), userLocale);
        String key_csrf_token = "dropbox-auth-csrf-token";
        DbxSessionStore csrfStore = new DbxStandardSessionStore(session, key_csrf_token);
        DbxAppInfo appInfo = new DbxAppInfo(config.getKey(), config.getSecret());

        auth = new DbxWebAuth(requestConfig, appInfo, callbackUrl, csrfStore);

        // Start authorization.
        String authorizePageUrl = auth.start();
        return authorizePageUrl;
    }

    /**
     * 
     * @return
     * @throws IllegalStateExcpetion
     *             , if user has no access.
     */
    public DbxClient client() {
        if (accessToken == null) {
            throw new IllegalStateException("No access token provided - has the user been authorized?");
        }
        if (dbxClient == null) {
            // Create a DbxClientV2, which is what you use to make API calls.
            String userLocale = Locale.getDefault().toString();
            DbxRequestConfig requestConfig = new DbxRequestConfig("FlexibleOrders", userLocale);

            dbxClient = new DbxClient(requestConfig, accessToken, DbxHost.Default);
        }
        return dbxClient;
    }

    public boolean hasAccess() {
        return accessToken != null;
    }

    public void setAuth(DbxWebAuth auth) {
        this.auth = auth;
    }

    /**
     * is called after successful authentication. Dropbox calls then the
     * redirect url and provides the access token.
     * 
     * @param parameterMap
     * @throws BadRequestException
     * @throws BadStateException
     * @throws CsrfException
     * @throws NotApprovedException
     * @throws ProviderException
     * @throws DbxException
     */
    public void finish(Map<String, String[]> parameterMap)
            throws BadRequestException,
            BadStateException,
            CsrfException,
            NotApprovedException,
            ProviderException,
            DbxException {
        DbxAuthFinish authFinish = auth.finish(parameterMap);
        accessToken = authFinish.accessToken;
    }

}
