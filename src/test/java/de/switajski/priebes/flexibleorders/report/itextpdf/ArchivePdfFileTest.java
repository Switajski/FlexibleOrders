package de.switajski.priebes.flexibleorders.report.itextpdf;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.Archive;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;

/**
 * The need of a pdf for archive is questionable
 * @author Marek
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
public class ArchivePdfFileTest {
	
	@Autowired ArchiveItemService archiveItemService;
	@Autowired ShippingItemService shippingItemService;
	
	Archive archive;
	
//	@Before
//	public void initData(){
//		//TODO: create a mock!
//		ArchiveItem oi1 = ArchiveItemTestFixture.createRandom();;
//		ArchiveItem oi2 = ArchiveItemTestFixture.createRandom();;
//		
//		oi2.setAccountNumber(oi1.getAccountNumber());
//		ArchiveItem merged = (ArchiveItem) archiveItemService.updateArchiveItem(oi2);
//		
//		ArrayList<ArchiveItem> archiveItems = new ArrayList<ArchiveItem>();
//		archiveItems.add(oi1);
//		archiveItems.add(merged);
//		
//		archive = new Archive(archiveItems);
//	}
	
	@Transactional
	@Test
	public void shouldGenerateArchive(){

		
		ArchivePdfFile bpf = new ArchivePdfFile();
        
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("Archive", archive);
			
			bpf.render(model, new MockHttpServletRequest(), new MockHttpServletResponse());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
}
