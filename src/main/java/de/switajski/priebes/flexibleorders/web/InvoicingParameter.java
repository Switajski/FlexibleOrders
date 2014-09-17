package de.switajski.priebes.flexibleorders.web;

import de.switajski.priebes.flexibleorders.web.dto.JsonCreateReportRequest;

public class InvoicingParameter {
    public JsonCreateReportRequest deliverRequest;

    public InvoicingParameter(JsonCreateReportRequest deliverRequest) {
        this.deliverRequest = deliverRequest;
    }
}