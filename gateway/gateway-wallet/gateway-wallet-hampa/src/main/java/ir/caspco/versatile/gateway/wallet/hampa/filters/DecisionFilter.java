package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.common.util.StringUtils;
import ir.caspco.versatile.common.util.reflect.GReflect;
import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.AuxiliaryBody;
import ir.caspco.versatile.gateway.smart.filters.annotations.ResponseBody;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.enums.ChargeWalletStatusEnum;
import ir.caspco.versatile.gateway.wallet.hampa.context.exceptions.DecisionException;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.FinancialResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.UUID;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@SuppressWarnings("unused")
public abstract class DecisionFilter<I, O, C> extends SmartGatewayFilter implements GReflect, InitializingBean {

    private Class<I> type;
    private String key;

    abstract protected void onCreate(I input, UUID uuid, DCash<C> cash);

    protected abstract O onSuccessWallet(ParameterModel<I, C> param);

    protected abstract ResultVO onFailWallet(ParameterModel<I, C> param);


    @Override
    public void afterPropertiesSet() {

        this.key = StringUtils.miniaturize(getClass().getSimpleName().replace("Filter", "") + "Result");

        this.type = firstGenericClassObject();

    }

    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResultVO injectResponseBody(ServerWebExchange exchange, ResultVO result, @AuxiliaryBody I inputMap, DCash<C> cash) {

        inputMap = Shift.just(inputMap).toShift(type).toObject();

        ParameterModel<I, C> param = (ParameterModel<I, C>) ParameterModel.builder()
                .exchange(exchange)
                .inputRequest(inputMap)
                .result(result)
                .cash((DCash<Object>) cash)
                .build();

        if (!HttpStatus.OK.equals(exchange.getResponse().getStatusCode())) {
            onFailWallet(param);

            throw new DecisionException(result.getResultNumber());
        }

        if (result.isError()) {
            return onFailWallet(param);
        }


        FinancialResultVO financialResult = Shift.just(result.getData())
                .toShift(FinancialResultVO.class).toObject();

        result.setData(Shift.just(result.getData()).toShift(Map.class).toObject());

        if (!ChargeWalletStatusEnum.DONE.status().equals(financialResult.getStatus())) {
            return onFailWallet(param);
        }

        Map data = (Map) result.getData();
        data.put(key, onSuccessWallet(param));

        return result;

    }
}

