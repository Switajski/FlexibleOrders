package de.switajski.priebes.flexibleorders.exceptions;

import org.apache.log4j.spi.ErrorCode;

public enum BusinessErrorCode implements ErrorCode{

    NOT_FOUND(101),
    WRONG_QUANTITY(102),
    CONTRADICTORY_PAYMENT_AGREEMENTS(110);
   
    private final int number;
   
    private BusinessErrorCode(int number) {
      this.number = number;
    }
    
    public int getNumber(){
        return number;
    }
    
}
