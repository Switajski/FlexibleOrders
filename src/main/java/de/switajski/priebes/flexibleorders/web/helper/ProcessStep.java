package de.switajski.priebes.flexibleorders.web.helper;

public enum ProcessStep {
    CONFIRMED("confirmed"),
    AGREED("agreed"),
    SHIPPED("shipped"),
    INVOICED("invoiced"),
    COMPLETED("completed"),
    ISSUED("issued"),
    DELIVERED("delivered");
    
    public final String mappedString;
    
    private ProcessStep(String value) {
        this.mappedString = value;
    }
    
    public static ProcessStep mapFromString(String abbr){
        for(ProcessStep v : values()){
            if( v.mappedString.equals(abbr)){
                return v;
            }
        }
        throw new IllegalArgumentException("No matching processStep for given String found");
    }
    
}
