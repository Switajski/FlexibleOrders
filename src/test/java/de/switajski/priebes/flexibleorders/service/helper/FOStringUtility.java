package de.switajski.priebes.flexibleorders.service.helper;

import org.apache.commons.lang3.StringUtils;

public class FOStringUtility {

	public static String camelCaseToSplitted(String camelCase) {
		String[] classNameSplitted = StringUtils.splitByCharacterTypeCamelCase(camelCase);
		StringBuilder usualNameBuilder = new StringBuilder();
		
		for (String s:classNameSplitted)
			usualNameBuilder.append(s).append(" ");
		return usualNameBuilder.toString();
	}
}
