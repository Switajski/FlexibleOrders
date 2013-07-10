package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Product {

    /**
     */
    @NotNull
    @Column(unique = true)
    private Long productNumber;

    /**
     */
    @NotNull
    @Enumerated
    private ProductType productType;

    /**
     */
    @NotNull
    private String name;

    /**
     */
    @NotNull
    private Boolean active;

    /**
     */
    private Long sortOrder;
}
