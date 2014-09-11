package de.switajski.priebes.flexibleorders.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/creditnotes")
@Controller
public class IssueController extends ExceptionController {

	@RequestMapping(value = "createissue", produces = "text/html")
	public String createissue(Model uiModel) {
		return "creditnotes/createissue";
	}

}
