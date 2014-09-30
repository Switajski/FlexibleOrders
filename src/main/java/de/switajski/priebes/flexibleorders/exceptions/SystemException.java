package de.switajski.priebes.flexibleorders.exceptions;

import org.apache.log4j.spi.ErrorCode;

public class SystemException extends IllegalArgumentException{

    private static final long serialVersionUID = 1L;
    
    ErrorCode code;
    
    public SystemException(String msg, ErrorCode code, Throwable cause) {
        super(msg, cause);
        this.code=code;
    }
    
    public SystemException(String msg, ErrorCode code) {
        super(msg);
        this.code=code;
    }
    
    public SystemException(ErrorCode code, Throwable cause) {
        super(cause);
        this.code=code;
    }
    
    public ErrorCode getErrorCode(){
        return code;
    }

}
