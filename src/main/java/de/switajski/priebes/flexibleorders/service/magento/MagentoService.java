package de.switajski.priebes.flexibleorders.service.magento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Service
public class MagentoService {

    private OAuthRestTemplate magentoRestTemplate;

    public List<String> getProducts() {
        try {
          InputStream photosXML = new ByteArrayInputStream(getMagentoRestTemplate().getForObject(URI.create("http://www.switajski.de/oauth"), byte[].class));

          final List<String> photoIds = new ArrayList<String>();
          SAXParserFactory parserFactory = SAXParserFactory.newInstance();
          parserFactory.setValidating(false);
          parserFactory.setXIncludeAware(false);
          parserFactory.setNamespaceAware(false);
          SAXParser parser = parserFactory.newSAXParser();
          parser.parse(photosXML, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
              if ("photo".equals(qName)) {
                photoIds.add(attributes.getValue("id"));
              }
            }
          });
          return photoIds;
        }
        catch (IOException e) {
          throw new IllegalStateException(e);
        }
        catch (SAXException e) {
          throw new IllegalStateException(e);
        }
        catch (ParserConfigurationException e) {
          throw new IllegalStateException(e);
        }
      }
    
    public OAuthRestTemplate getMagentoRestTemplate() {
        return magentoRestTemplate;
    }

    public void setMagentoRestTemplate(OAuthRestTemplate magentoRestTemplate) {
        this.magentoRestTemplate = magentoRestTemplate;
    }
    
}
