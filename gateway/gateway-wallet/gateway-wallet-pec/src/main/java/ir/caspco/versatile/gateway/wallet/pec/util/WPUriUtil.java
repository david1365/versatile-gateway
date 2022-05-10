package ir.caspco.versatile.gateway.wallet.pec.util;

import ir.caspco.versatile.common.util.MyProperties;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.gateway.wallet.pec.context.properties.WPProperties;
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

@Component("WPUriUtil")
public class WPUriUtil implements UriUtil {

    private final MyProperties myProperties;

    private final WPProperties wpProperties;

    public WPUriUtil(MyProperties myProperties, WPProperties wpProperties) {
        this.myProperties = myProperties;
        this.wpProperties = wpProperties;
    }

    @SneakyThrows
    @Override
    public URI getPrefixUri() {
        return new URI(myProperties.myUri().toString() + wpProperties.getRoute().getPath().replace("/**", ""));
    }

    @SneakyThrows
    @Override
    public URI getRealPrefixUri() {
        return new URI(wpProperties.getRoute().getUri());
    }

}

