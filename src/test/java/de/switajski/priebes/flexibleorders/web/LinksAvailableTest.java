package de.switajski.priebes.flexibleorders.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class LinksAvailableTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	@Test
	public void getFoo() throws Exception {
		this.mockMvc.perform(get("reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo2() throws Exception {
		this.mockMvc.perform(get("/reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo3() throws Exception {
		this.mockMvc.perform(get("FlexibleOrders/reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo4() throws Exception {
		this.mockMvc.perform(get("/FlexibleOrders/reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo5() throws Exception {
		this.mockMvc.perform(get("http://localhost:8080/FlexibleOrders/reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo6() throws Exception {
		this.mockMvc.perform(get("/reports/orders/listOrderNumbers").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
//		.andExpect(content().mimeType())
		.andExpect(jsonPath("$.name").value("Lee"));
	}

}
