package kz.epam.InternetShop.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConfig implements TomcatConnectorCustomizer {

    @Override
    public void customize(Connector connector) {
        connector.setAttribute("relaxedQueryChars", "[]|{}^&#x5c;&#x60;&quot;&lt;&gt;");
        connector.setAttribute("relaxedPathChars", "[]|");
    }
}
