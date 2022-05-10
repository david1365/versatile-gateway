package ir.caspco.versatile.gateway.wallet.hampa.util;

import ir.caspco.versatile.common.util.MyProperties;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
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

@Component("WHUriUtil")
public class HampaUriUtil implements UriUtil {

    private final MyProperties myProperties;

    private final HampaProperties wHProperties;

    public HampaUriUtil(MyProperties myProperties, HampaProperties wHProperties) {
        this.myProperties = myProperties;
        this.wHProperties = wHProperties;
    }


    @SneakyThrows
    @Override
    public URI getPrefixUri() {
        return new URI(myProperties.myUri().toString() + wHProperties.getRoute().getPath().replace("/**", ""));
    }

    @SneakyThrows
    @Override
    public URI getRealPrefixUri() {
        return new URI(wHProperties.getRoute().getUri());
    }
}

