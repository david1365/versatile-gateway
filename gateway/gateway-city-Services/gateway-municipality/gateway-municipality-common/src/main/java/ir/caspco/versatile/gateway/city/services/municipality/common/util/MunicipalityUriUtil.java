package ir.caspco.versatile.gateway.city.services.municipality.common.util;

import ir.caspco.versatile.common.util.MyProperties;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityProperties;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@Component("MunicipalityUriUtil")
public class MunicipalityUriUtil implements UriUtil {

    private final MyProperties myProperties;

    private final MunicipalityProperties municipalityProperties;

    public MunicipalityUriUtil(MyProperties myProperties, MunicipalityProperties municipalityProperties) {
        this.myProperties = myProperties;
        this.municipalityProperties = municipalityProperties;
    }

    @SneakyThrows
    @Override
    public URI getPrefixUri() {
        return new URI(myProperties.myUri().toString() + municipalityProperties.getRoute().getPath().replace("/**", ""));
    }

    @SneakyThrows
    @Override
    public URI getRealPrefixUri() {
        return new URI(municipalityProperties.getRoute().getUri());
    }

}

