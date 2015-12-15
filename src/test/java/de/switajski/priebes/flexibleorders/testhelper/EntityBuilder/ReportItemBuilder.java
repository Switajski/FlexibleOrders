package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public abstract class ReportItemBuilder<BEAN extends ReportItem, BUILDER extends Builder<BEAN>> implements Builder<BEAN> {

    protected int quantity;
    protected Report report = null;
    protected ReportItem predecessor;
    protected Set<ReportItem> successors;
    protected OrderItem item;
    protected Date created;

    public BEAN build(BEAN bean) {
        bean.setQuantity(quantity);
        if (report != null) report.addItem(bean);
        if (item != null) bean.setOrderItem(item);
        bean.setCreated(created);
        bean.setPredecessor(predecessor);
        if (predecessor != null) {
            if (predecessor.getSuccessors() == null) {
                predecessor.setSuccessors(new HashSet<ReportItem>());
            }
            if (!predecessor.getSuccessors().contains(predecessor)) {
                predecessor.getSuccessors().add(predecessor);
            }
        }
        bean.setSuccessors(successors);
        return bean;
    }

    public ReportItemBuilder<BEAN, BUILDER> setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public ReportItemBuilder<BEAN, BUILDER> setReport(Report report) {
        this.report = report;
        return this;
    }

    public ReportItemBuilder<BEAN, BUILDER> setItem(OrderItem item) {
        this.item = item;
        return this;
    }

    public ReportItemBuilder<BEAN, BUILDER> setDate(Date created) {
        this.created = created;
        return this;
    }

    public ReportItemBuilder<BEAN, BUILDER> setPredecessor(ReportItem predecessor) {
        this.predecessor = predecessor;
        return this;
    }

    public ReportItemBuilder<BEAN, BUILDER> setSuccessor(Set<ReportItem> successors) {
        this.successors = successors;
        return this;
    }

}
