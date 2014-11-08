package de.switajski.priebes.flexibleorders.web.helper;

public enum ProductionState {
    CONFIRMED("confirmed"),
    AGREED("agreed"),
    SHIPPED("shipped"),
    INVOICED("invoiced"),
    COMPLETED("completed"),
    ISSUED("issued"),
    DELIVERED("delivered");
    
    public final String mappedString;
    
    private ProductionState(String value) {
        this.mappedString = value;
    }
    
    public static ProductionState mapFromString(String abbr){
        for(ProductionState v : values()){
            if( v.mappedString.equals(abbr)){
                return v;
            }
        }
        throw new IllegalArgumentException("No matching productionState for given String : \""+ abbr +"\" found");
    }
    
}
