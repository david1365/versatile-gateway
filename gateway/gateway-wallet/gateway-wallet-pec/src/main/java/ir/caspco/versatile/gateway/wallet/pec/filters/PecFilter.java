package ir.caspco.versatile.gateway.wallet.pec.filters;


import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.RequestBody;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;
import java.util.Map;

import static ir.caspco.versatile.gateway.smart.filters.annotations.Filter.ALL;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@Filter(prefix = "wallet-pec.prefix")
@SuppressWarnings("unused")
public class PecFilter extends SmartGatewayFilter {

//    private final WalletRepository walletRepository;
//    private final CustomerRepository customerRepository;
//
//
//    public WPMutatedFilter(HeaderUtil headerUtil, WalletRepository walletRepository, CustomerRepository customerRepository) {
//        this.walletRepository = walletRepository;
//        this.customerRepository = customerRepository;
//    }

    @RequestBody(ALL)
    public Map<String, Object> injectForAll(Map<String, Object> body) {

        body.put("CorporationPIN", "93847A06561D5646067685995156FD01");

        return body;

    }

    @RequestBody("wallet-pec.paths.AddCustomer")
    public Map<String, Object> injectForAddCustomer(Map<String, Object> body) {

        body.put("IsActive", true);

        return body;

    }

    @RequestBody({
            "wallet-pec.paths.AddWallet",
            "wallet-pec.paths.GetMerchantWallet"
            ,})
    public Map<String, Object> injectForWallet(Map<String, Object> body) {

        body.put("GroupWalletId", 1);

        return body;

    }

    @RequestBody({
            "wallet-pec.paths.GetMerchantWallet"
            ,})
    public Map<String, Object> injectForGetMerchantWallet(Map<String, Object> body) {

        body.put("CorporationUserId", "0");

        return body;

    }

    @RequestBody({
            "wallet-pec.paths.ChargeWallet",
            "wallet-pec.paths.GetCustomerTransaction",
            "wallet-pec.paths.DeChargeWallet"
            ,})
    public Map<String, Object> injectForShared(Map<String, Object> body) {

        body.put("MediaTypeId", 12);

        return body;

    }

    @RequestBody({
            "wallet-pec.paths.ChargeWallet"
            ,})
    public Map<String, Object> injectForChargeWallet(Map<String, Object> body) {

        body.put("STerminalNumber", 0);
        body.put("SPosConditionCode", 59);

        return body;

    }

    @RequestBody({
            "wallet-pec.paths.ChargeWallet",
            "wallet-pec.paths.DeChargeWallet"
    })
    public Map<String, Object> injectIpAddress(ServerWebExchange exchange, Map<String, Object> body) {

        InetAddress inetAddress = exchange.getRequest().getRemoteAddress().getAddress();
        String hostAddress = inetAddress == null ? null : inetAddress.getHostAddress();

        body.put("IpAddress", hostAddress);

        return body;

    }
//
//    public Shift<Map<String, Object>> saveRequest(ServerWebExchange exchange, Shift<Map<String, Object>> body) {
//
//        putAttribute(exchange, requestId, body);
//
//        return body;
//
//    }
//
//    public Shift<Map<String, Object>> saveCustomerResponse(ServerWebExchange exchange, Shift<Map<String, Object>> body) {
//
//        return saveResponse(exchange, requestId, body, (savedBody, key) ->
//                savedBody.toShift(CustomerEntity.class).subscribe(customer -> {
//
//                    customer.setReturnId(key.getReturnId());
//
//                    customerRepository.save(customer);
//
//                })
//        );
//
//    }
//
//
//    public Shift<Map<String, Object>> saveWalletResponse(ServerWebExchange exchange, Shift<Map<String, Object>> body) {
//
//        return saveResponse(exchange, requestId, body, (savedBody, key) ->
//                savedBody.toShift(WalletEntity.class).subscribe(wallet -> {
//
//                    wallet.setReturnId(key.getReturnId());
//
//                    walletRepository.save(wallet);
//
//                })
//        );
//
//    }
//
//    private Shift<Map<String, Object>> saveResponse(ServerWebExchange exchange,
//                                                    String requestId,
//                                                    Shift<Map<String, Object>> body,
//                                                    BiConsumer<Shift<Map<String, Object>>, KeyVO> Consumer) {
//
//        HttpStatus statusCode = exchange.getResponse().getStatusCode();
//
//        if (!Objects.equals(statusCode, HttpStatus.OK)) {
//
//            removeAttribute(exchange, requestId);
//
//            return body;
//
//        }
//
//        return body.toShift(ResultVO.class).subscribe(result -> {
//
//            Shift<Map<String, Object>> savedBody = getAttribute(exchange, requestId);
//
//            if (savedBody != null) {
//
//                KeyVO key = result.getKeys().getKey();
//
//                if (key.getResultId() == 0) {
//                    Consumer.accept(savedBody, key);
//                }
//
//                removeAttribute(exchange, requestId);
//            }
//
//        });
//
//    }
//
//    private void removeAttribute(ServerWebExchange exchange, String requestId) {
//        exchange.getAttributes().remove(requestId);
//    }
//
//    private void putAttribute(ServerWebExchange exchange, String requestId, Object value) {
//        exchange.getAttributes().put(requestId, value);
//    }
//
//    private Shift<Map<String, Object>> getAttribute(ServerWebExchange exchange, String requestId) {
//        return exchange.getAttribute(requestId);
//    }
}

