package de.switajski.priebes.flexibleorders.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.switajski.priebes.flexibleorders.service.OrderServiceImpl;
import de.switajski.priebes.flexibleorders.service.ReportItemServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp/WEB-INF/spring")
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext*.xml"})
@Ignore("Tests were never successful. Propably due to missing Controllers")
public class LinksAvailableTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
    private FilterChainProxy springSecurityFilter;
	
	@Autowired
	private ReportItemServiceImpl ris;
	
	@Autowired
	private OrderServiceImpl asdf;
	
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
//		this.mockMvc = webAppContextSetup(this.wac).addFilter(springSecurityFilter).build();
		this.mockMvc = webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void getFoo() throws Exception {
		this.mockMvc
				.perform(
						get("/FlexibleOrders/ordered").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
				// .andExpect(content().mimeType())
//				.andExpect(jsonPath("$.name").value("Lee"));
	}
	
	@Test
	public void getFoo2() throws Exception {
		this.mockMvc
				.perform(
						get("FlexibleOrders/ordered").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFoo3() throws Exception {
		this.mockMvc
				.perform(
						get("/ordered").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFoo4() throws Exception {
		this.mockMvc
				.perform(
						get("ordered").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFoo5() throws Exception {
		this.mockMvc
				.perform(
						get("/test/test").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFo6() throws Exception {
		this.mockMvc
				.perform(
						get("test/test").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFo8() throws Exception {
		this.mockMvc
				.perform(
						get("/FlexibleOrder/test/test").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getFo9() throws Exception {
		this.mockMvc
				.perform(
						get("FlexibleOrder/test/test").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFo10() throws Exception {
		this.mockMvc
				.perform(
						get("/").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFo120() throws Exception {
		this.mockMvc
				.perform(
						get("/FlexibleOrders").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getFo1220() throws Exception {
		this.mockMvc
				.perform(
						get("FlexibleOrders").accept(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
