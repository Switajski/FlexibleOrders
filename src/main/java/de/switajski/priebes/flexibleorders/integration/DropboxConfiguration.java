package de.switajski.priebes.flexibleorders.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holds the properties from dropbox.properties in a bean.
 * 
 * @author Marek
 *
 */
@Component
public class DropboxConfiguration {

    private String client_id, key, secret;

    @Value("${dropbox.client_id}")
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Value("${dropbox.key}")
    public void setKey(String key) {
        this.key = key;
    }

    @Value("${dropbox.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getClientId() {
        return client_id;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

}
