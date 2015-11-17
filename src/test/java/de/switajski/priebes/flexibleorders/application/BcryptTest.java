package de.switajski.priebes.flexibleorders.application;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {

    @Test
    public void startEncryption() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("00TAY9fci5V");

        System.out.println(hashedPassword);
    }
}
