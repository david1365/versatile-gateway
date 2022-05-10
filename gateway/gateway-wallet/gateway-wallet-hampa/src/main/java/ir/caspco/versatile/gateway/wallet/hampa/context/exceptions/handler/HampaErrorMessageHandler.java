package ir.caspco.versatile.gateway.wallet.hampa.context.exceptions.handler;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessageHandler;
import ir.caspco.versatile.context.handler.exceptions.message.DefaultMessage;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorDescription;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorMessageHandler;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Qualifier("hampaErrorMessageHandler")
@MessageHandler("ir.caspco.versatile.gateway.wallet.hampa")
public class HampaErrorMessageHandler implements ErrorMessageHandler<DefaultMessage> {
    @Override
    public DefaultMessage message(Throwable error, List<ErrorDescription> descriptions) {
        return DefaultMessage.builder()
                .error(true)
                .resultNumber(errorId(error))
                .data(descriptions)
                .build();
    }
}
