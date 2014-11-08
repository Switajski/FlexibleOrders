package de.switajski.priebes.flexibleorders.reference;


public enum ProductType {
	SHIPPING("SHIPPING"), CUSTOM("CUSTOM"), PRODUCT("PRODUCT");
	
	public final String mappedString;
    
    private ProductType(String value) {
        this.mappedString = value;
    }
	
	public static ProductType mapFromString(String abbr){
        for (ProductType v : values()){
            if( v.mappedString.equals(abbr)){
                return v;
            }
        }
        throw new IllegalArgumentException("No matching productType for given String: \""+ abbr +"\" found");
    }
}
