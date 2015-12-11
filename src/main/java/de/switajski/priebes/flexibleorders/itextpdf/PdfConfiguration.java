package de.switajski.priebes.flexibleorders.itextpdf;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

@Component
public class PdfConfiguration {

    Image logo;

    public Image logo() {
        String name = "images/LogoGross.jpg";
        if (logo == null) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                logo = Image.getInstance(classLoader.getResource(name));
                return logo;
            }
            catch (BadElementException | IllegalStateException | IOException e) {
                throw new IllegalArgumentException("Could not find logo file at specified path name:" + name);
            }
        }
        else return logo;
    }
}
