package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import de.switajski.priebes.flexibleorders.reference.Country;
import javax.persistence.Enumerated;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Customer {

    /**
     */
    @Column(unique = true)
    private String shortName;

    /**
     */
    private String name1;

    /**
     */
    private String name2;

    /**
     */
    @NotNull
    private String street;

    /**
     */
    @NotNull
    private String city;

    /**
     */
    @NotNull
    private int postalCode;

    /**
     */
    @NotNull
    @Enumerated
    private Country country;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();
}
