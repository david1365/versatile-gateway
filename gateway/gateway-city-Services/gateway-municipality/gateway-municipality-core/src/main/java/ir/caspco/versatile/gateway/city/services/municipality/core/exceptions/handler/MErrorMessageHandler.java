package ir.caspco.versatile.gateway.city.services.municipality.core.exceptions.handler;

import ir.caspco.versatile.context.handler.exceptions.message.ErrorDescription;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorMessageHandler;
import ir.caspco.versatile.gateway.city.services.municipality.context.exceptions.model.MunicipalityMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//@Qualifier("mErrorMessageHandler")
//@MessageHandler("ir.caspco.versatile.gateway.city.services.municipality")
public class MErrorMessageHandler implements ErrorMessageHandler<MunicipalityMessage> {
    @Override
    public MunicipalityMessage message(Throwable error, List<ErrorDescription> descriptions) {

        List<String> messages = descriptions.stream()
                .map(errorDescription -> errorDescription.getFa_IR())
                .collect(Collectors.toList());


        return MunicipalityMessage.builder()
                .messages(messages)
                .build();

    }
}
