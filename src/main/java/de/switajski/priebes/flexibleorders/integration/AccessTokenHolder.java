package de.switajski.priebes.flexibleorders.integration;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxWebAuth;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccessTokenHolder {

    private DbxWebAuth auth;

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public DbxWebAuth getAuth() {
        return auth;
    }

    public void setAuth(DbxWebAuth auth) {
        this.auth = auth;
    }

}
