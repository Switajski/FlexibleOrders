package de.switajski.priebes.flexibleorders.domain;

//TODO: refactor to use Polymorphism
@Deprecated
public enum ReportItemType {
	
	CONFIRM, 
	FORWARD_TO_THIRD_PARTY, 
	SHIP, 
	INVOICE,
	PAID, 
	CANCEL; 
	
}
