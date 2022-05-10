package ir.caspco.versatile.gateway.wallet.hampa.context.model;

import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@Builder
public class ParameterModel<I, E> {
    ServerWebExchange exchange;
    ResultVO result;
    I inputRequest;
    DCash<E> cash;
}
