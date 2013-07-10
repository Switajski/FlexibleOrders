package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import de.switajski.priebes.flexibleorders.reference.Status;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaEntity(inheritanceType = "TABLE_PER_CLASS")
public abstract class Item {

    /**
     */
    @NotNull
    @OneToOne
    private Product product;

    /**
     */
    @NotNull
    @ManyToOne
    private Customer customer;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    /**
     */
    @NotNull
    private int quantity;

    /**
     */
    @Min(0L)
    private BigDecimal priceNet;

    /**
     */
    @NotNull
    @Enumerated
    private Status status;

    /**
     */
    @NotNull
    private String productName;

    /**
     */
    @NotNull
    private Long productNumber;

    /**
     */
    private Long orderConfirmationNumber;

    /**
     */
    private Long invoiceNumber;

    /**
     */
    private Long accountNumber;

    /**
     */
    @NotNull
    private Long orderNumber;
}
