package de.switajski.priebes.flexibleorders.domain;

//TODO: refactor to use Polymorphism
/**
 * @deprecated
 * @author Marek Switajski
 *
 */
@Deprecated
public enum ReportItemType {
	ORDER,
	CONFIRM, 
	FORWARD_TO_THIRD_PARTY, 
	SHIP, 
	INVOICE,
	PAID, 
	CANCEL; 
	
}
