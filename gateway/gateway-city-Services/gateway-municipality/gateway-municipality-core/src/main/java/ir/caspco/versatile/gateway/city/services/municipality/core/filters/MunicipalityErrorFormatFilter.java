package ir.caspco.versatile.gateway.city.services.municipality.core.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.handler.exceptions.Translator;
import ir.caspco.versatile.context.handler.exceptions.message.DefaultMessage;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorDescription;
import ir.caspco.versatile.gateway.city.services.municipality.context.exceptions.model.MunicipalityMessage;
import ir.caspco.versatile.gateway.city.services.municipality.context.exceptions.model.MunicipalitySnMessage;
import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.ResponseBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class MunicipalityErrorFormatFilter extends SmartGatewayFilter {

    private final Translator translator;


    public MunicipalityErrorFormatFilter(@Qualifier("defaultTranslator") Translator translator) {
        this.translator = translator;
    }


    @ResponseBody(ALL)
    public DefaultMessage injectResponseBody(ServerWebExchange exchange, Object result) {
        String defaultMessage = translator.getMessage(
                "ir.caspco.versatile.gateway.city.services.municipality.core.filters.MunicipalityErrorFormatFilter.defaultMessage"
                , Translator.ENGLISH);

        if (result instanceof Map) {

            MunicipalitySnMessage singleMessage = Shift.just(result).toShift(MunicipalitySnMessage.class).toObject();
            if (singleMessage.getMessage() != null) {
                List<ErrorDescription> errorDescriptions = new ArrayList<>();

                errorDescriptions.add(

                        ErrorDescription.builder()
                                .en_US(defaultMessage)
                                .fa_IR(singleMessage.getMessage())
                                .build()

                );

                return errorMessage(errorDescriptions, exchange.getResponse());
            }

        }

        if (!HttpStatus.OK.equals(exchange.getResponse().getStatusCode())) {

            MunicipalityMessage municipalityMessage = Shift.just(result).toShift(MunicipalityMessage.class).toObject();

            List<ErrorDescription> errorDescriptions = new ArrayList<>();


            municipalityMessage.getMessages().forEach(message ->
                    errorDescriptions.add(

                            ErrorDescription.builder()
                                    .en_US(defaultMessage)
                                    .fa_IR(message)
                                    .build()

                    )
            );

            return errorMessage(errorDescriptions, exchange.getResponse());
        }

        return DefaultMessage.builder()
                .error(false)
                .resultNumber(HttpStatus.OK.value())
                .data(result)
                .build();

    }

    private DefaultMessage errorMessage(Object result, ServerHttpResponse httpResponse) {

        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);

        return DefaultMessage.builder()
                .error(true)
                .resultNumber(HttpStatus.BAD_REQUEST.value())
                .data(result)
                .build();

    }

}

