package de.switajski.priebes.flexibleorders.web;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;

@Controller
public class ExceptionController {

    private static Logger log = Logger.getLogger(ExceptionController.class);
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception ex) {
        return handleExceptionAsError(ex);
    }

    @ExceptionHandler(NotImplementedException.class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public String receiveException(NotImplementedException ex) {
        return handleExceptionAsNotification(ex, "Funktion noch nicht implementiert");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleExceptionAsNotification(ex, "Funktion mit falschen Parameter aufgerufen");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleNotFoundException(NotFoundException ex) {
        return handleExceptionAsNotification(ex, "Nicht gefunden");
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(JsonParseException ex) {
        return handleExceptionAsError(ex);
    }

    private String handleExceptionAsError(Exception ex) {
        log.error(ex.getMessage(), ex);
        if (ex.getMessage() == null) return stringifyWithStackTrace(ex, "Fehler beim Server: ");
        return stringify(ex);
    }

    private String handleExceptionAsNotification(Exception ex, String messageToUser) {
        if (ex.getMessage() == null) return messageToUser;
        return stringify(ex);
    }

    private String stringifyWithStackTrace(Exception ex, String message) {
        StringBuilder builder = new StringBuilder("<b>")
                .append(message)
                .append(ex.getClass().getSimpleName())
                .append("</b>");
        if (ex.getMessage() != null) builder.append("</b> :</br> ")
                .append(ex.getMessage());
        builder.append("</br></br>Stack Trace:</br>");
        for (StackTraceElement line : ex.getStackTrace())
            builder.append(line.toString()).append("</br>");
        return builder.toString();
    }

    private String stringify(Exception ex) {
        return "<b>" + ex.getClass().getSimpleName() + "</b> :</br> " + ex.getMessage();
    }

}
