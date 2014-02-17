package de.switajski.priebes.flexibleorders.web;

import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Controller
public class ExceptionController {
	
	private static Logger log = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleException(Exception ex) {
		log.error(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Fehler beim Server: " + ex.getClass().getSimpleName();
		return stringify(ex);
	}

	@ExceptionHandler(NotImplementedException.class)
	@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
	@ResponseBody
	public String handleException(NotImplementedException ex) {
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Funktion noch nicht implementiert";
		return stringify(ex);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleIllegalArgumentException(IllegalArgumentException ex) {
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Funktion mit falschen Parameter aufgerufen";
		return stringify(ex);
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleNotFoundException(NotFoundException ex) {
		log.warn(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Nicht gefunden";
		return stringify(ex);
	}
	
	@ExceptionHandler(JsonParseException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleException(JsonParseException ex) {
		log.error(ex.getClass().getSimpleName(), ex);
		if (ex.getMessage() == null) return "Fehler beim Parsen der Anfrage";
		return stringify(ex);
	}
	
	private String stringify(Exception ex) {
		return "<b>" + ex.getClass().getSimpleName() + "</b> :</br> "+ ex.getMessage();
	}
	
}
