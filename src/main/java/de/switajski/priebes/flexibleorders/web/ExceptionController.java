package de.switajski.priebes.flexibleorders.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonParseException;

import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;

@CrossOrigin
@Controller
public class ExceptionController {

    private static Logger log = Logger.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception ex) {
        return handleExceptionAsError(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleExceptionAsNotification(ex, "Funktion mit falschen Parameter aufgerufen");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody JsonObjectResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        JsonObjectResponse response = new JsonObjectResponse();
        response.setSuccess(false);
        response.setMessage("Validation error");
        Map<String, String> errors = new HashMap<String, String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getCode());
        }
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
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

    public String handleExceptionAsError(Exception ex) {
        log.error(ex.getMessage(), ex);
        if (ex.getMessage() == null) return stringifyWithStackTrace(ex, "Fehler beim Server: ");
        return stringify(ex);
    }

    public String handleExceptionAsNotification(Exception ex, String messageToUser) {
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
