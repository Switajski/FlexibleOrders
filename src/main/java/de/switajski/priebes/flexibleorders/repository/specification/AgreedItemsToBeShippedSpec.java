package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class AgreedItemsToBeShippedSpec extends AbstractOpenReportItemSpecification{

    private Root<ReportItem> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder cb;

    
    public AgreedItemsToBeShippedSpec() {
        super(ConfirmationItem.class, ShippingItem.class);
    }
    
//    @Override
//    public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//        this.root = root;
//        this.query = query;
//        this.cb = cb;
//        
//        Subquery<ConfirmationItem> subquery = createCiSubquery();
//        return cb.in(root).value(subquery);
//    }
//
//    private Subquery<ConfirmationItem> createCiSubquery() {
//        Subquery<ConfirmationItem> subquery = query.subquery(ConfirmationItem.class);
//        Root<ConfirmationItem> ciRoot = subquery.from(ConfirmationItem.class);
//        subquery.select(ciRoot);
//        return subquery;
//    }


    // /**
    // *
    // http://stackoverflow.com/questions/9875943/how-to-search-on-subclass-attribute-with-a-jpa-criteriaquery
    // */
    // @Override
    // public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?>
    // query, CriteriaBuilder cb) {
    // Subquery<OrderConfirmation> subquery =
    // query.subquery(OrderConfirmation.class);
    // Root<OrderConfirmation> confirmationItemRoot =
    // subquery.from(OrderConfirmation.class);
    //
    // subquery.select(confirmationItemRoot);
    // subquery.where(cb.equal(confirmationItemRoot.<OrderConfirmation>get("orderAgreementNumber"),
    // null));
    //
    // //
    // subquery.where(cb.equal(confirmationItemRoot.<OrderConfirmation>get("orderAgreementNumber"),
    // null));
    //
    // return cb.in(root).value(subquery);
    // }

}
