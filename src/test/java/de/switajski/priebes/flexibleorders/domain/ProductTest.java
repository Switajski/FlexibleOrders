package de.switajski.priebes.flexibleorders.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;

public class ProductTest {

	@Test
	public void equals_ifEqualProductsShouldReturnTrue(){
		// GIVEN
		Product amy1 = CatalogProductBuilder.buildAmy().toProduct();
		Product amy2 = CatalogProductBuilder.buildAmy().toProduct();
		
		//WHEN / THEN
		assertThat(amy1.equals(amy2), is(true));
	}
}
