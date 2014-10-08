package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

/**
 * Specifies "open" ReportItems: </br> A concrete ReportItem is open, when its
 * quantity differs from an other ReportItem. </br> </br><b>Example:</b>
 * Quantity of a shipped item (ShippingItem) is 10. The same order item has only
 * an invoicing item with quantity 5 (InvoicingItem). Consequently 5 are not
 * invoiced yet. So they are open.</br> </br> <b>Usage:</b> In the case of given
 * example the method <code>reportItemClassToRetrieve</code> should be a
 * ShippingItem.class, <code>reportItemClassToSubtract</code> should be a
 * InvoiceItem.class
 * 
 * @author Marek Switajski
 * 
 */
public class AbstractOpenReportItemSpecification implements
        Specification<ReportItem> {

    private static final String QTY = "quantity";
    private static final String ORDER_ITEM = "orderItem";
    CriteriaBuilder cb;
    CriteriaQuery<?> query;
    Root<ReportItem> root;
    private Class<? extends ReportItem> reportItemClassToSubtract;
    private Class<? extends ReportItem> reportItemClassToRetrieve;

    /**
     * 
     * @param reportItemClassToRetrieve 
     * @param reportItemClassToSubtract
     */
    public AbstractOpenReportItemSpecification(
            Class<? extends ReportItem> reportItemClassToRetrieve,
            Class<? extends ReportItem> reportItemClassToSubtract) {
        this.reportItemClassToRetrieve = reportItemClassToRetrieve;
        this.reportItemClassToSubtract = reportItemClassToSubtract;
    }
    
    /**
     * 
     * Specification in JQL would look like this:
     * 
     * <pre>
     * select from ConfirmationItem ri where 
     *       EXISTS //inCondition
     *       // subquery
     *       (SELECT he from ReportItem he where he.orderItem = ri.orderItem and 
     *           (SELECT sum(confirmEvent.quantity) from ConfirmationItem confirmEvent 
     *           where confirmEvent.orderItem = ri.orderItem)
     *            > "//Subquery2
     *           (SELECT coalesce(sum(shipEvent.quantity),0) from ShippingItem shipEvent 
     *           where shipEvent.orderItem = ri.orderItem) 
     *       );
     * </pre>
     * 
     * @see http://stackoverflow.com/questions/3997070/jpa-criteria-tutorial
     */
    @Override
    public Predicate toPredicate(Root<ReportItem> root,
            CriteriaQuery<?> query, CriteriaBuilder cb) {
        this.cb = cb;
        this.query = query;
        this.root = root;

        Subquery<ReportItem> subquery = query.subquery(ReportItem.class);
        Root<ReportItem> fromSubquery = subquery.from(ReportItem.class);
        subquery.where(cb.and(
                cb.equal(root.<OrderItem> get(ORDER_ITEM), fromSubquery.get(ORDER_ITEM)),
                cb.greaterThan(
                        createQtySum(reportItemClassToRetrieve),
                        createQtySum(reportItemClassToSubtract)
                        )));
        subquery.select(fromSubquery);

        Predicate inCondition = cb.and(cb.exists(subquery), cb.equal(root.type(), reportItemClassToRetrieve));
        return inCondition;
    }

    private Subquery<Integer> createQtySum(
            Class<? extends ReportItem> specificReportItem) {
        Subquery<Integer> seSq = query.subquery(Integer.class);
        Root<? extends ReportItem> fromSi = seSq.from(specificReportItem);
        Expression<Integer> select = cb.<Integer> sum(fromSi.<Integer> get(QTY));
        Expression<Integer> coalescted = cb.coalesce(select, new Integer(0));
        seSq.select(coalescted);
        seSq.where(cb.equal(root.get(ORDER_ITEM), fromSi.get(ORDER_ITEM)));
        return seSq;
    }

}
