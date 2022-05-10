package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.handler.exceptions.Translator;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorDescription;
import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.ResponseBody;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.List;

import static ir.caspco.versatile.gateway.smart.filters.annotations.Filter.ALL;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@SuppressWarnings("unused")
public class HampaErrorFormatFilter extends SmartGatewayFilter {

    private final Translator translator;


    public HampaErrorFormatFilter(@Qualifier("defaultTranslator") Translator translator) {
        this.translator = translator;
    }


    @ResponseBody(ALL)
    public ResultVO injectResponseBody(ServerWebExchange exchange, ResultVO result) {

//        if (!HttpStatus.OK.equals(exchange.getResponse().getStatusCode())) {

        if (result.isError()) {

            String key = String.valueOf(result.getResultNumber());

            List<ErrorDescription> errorDescriptions = new ArrayList<>();

            errorDescriptions.add(

                    ErrorDescription.builder()
                            .en_US(translator.getMessage(key, Translator.ENGLISH))
                            .fa_IR(translator.getMessage(key, Translator.PERSIAN))
                            .build()

            );


            result.setData(errorDescriptions);

        }

//        }

        return result;

    }

}

