package de.switajski.priebes.flexibleorders.integration;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxWebAuth;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccessTokenHolder {

    private DbxWebAuth auth;

    private DbxAuthFinish authFinish;

    public String getAccessToken() {
        if (authFinish == null) return null;
        return authFinish.accessToken;
    }

    public void setAuthFinish(DbxAuthFinish authFinish) {
        this.authFinish = authFinish;
    }

    public DbxWebAuth getAuth() {
        return auth;
    }

    public void setAuth(DbxWebAuth auth) {
        this.auth = auth;
    }

}
