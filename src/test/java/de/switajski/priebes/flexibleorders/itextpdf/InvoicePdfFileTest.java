package de.switajski.priebes.flexibleorders.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.InvoicePdfFile;

public class InvoicePdfFileTest {

	private static final String INVOICE_PDF_PATH = "src/test/resources/InvoicePdfFileTest.pdf";

	@Transactional(readOnly = true)
	@Test
	public void shouldGenerateInvoice() throws Exception {

		InvoicePdfFile bpf = new InvoicePdfFile();
		bpf.setFilePathAndName(INVOICE_PDF_PATH);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		ReportDto reportDto = ReportDtoTestFixture.givenReportDto();
		model.put(reportDto.getClass().getSimpleName(), reportDto);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
