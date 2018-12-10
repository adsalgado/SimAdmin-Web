package mx.sharkit.web;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Adrián Salgado D.
 */
@Configuration
public class JsfConfigurationMimeMapper implements EmbeddedServletContainerCustomizer {

     @Override
     public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("xsd", "text/xml; charset=utf-8");
        mappings.add("otf", "font/opentype");
        mappings.add("ico", "image/x-icon");
        mappings.add("svg", "image/svg+xml");
        mappings.add("eot", "application/vnd.ms-fontobject");
        mappings.add("ttf", "application/x-font-ttf");
        mappings.add("woff", "application/x-font-woff");
        mappings.add("woff2", "application/x-font-woff2");
        mappings.add("xhtml", "application/xml");
        container.setMimeMappings(mappings);
    }
    
}
