package ir.caspco.versatile.gateway.wallet.hampa.client;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

import ir.caspco.versatile.context.rest.client.annotations.*;
import ir.caspco.versatile.context.rest.client.interfaces.Post;
import ir.caspco.versatile.gateway.wallet.hampa.client.exceptions.WalletClientException;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.WalletInfoVO;
import ir.caspco.versatile.rest.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Headers({@Header(key = HttpHeaders.AUTHORIZATION, value = "gateway.authorization")})
@ApplicationBaseUrl("gateway.uri")
@JsonDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")
@ThrowException(WalletClientException.class)
public class WalletClient extends RestClient {

    @ResponsePath(servicePath = "wallet-hampa.client.getWalletInfo", outputType = ResultVO.class)
    public Post<WalletInfoVO, Void, ResultVO> getWalletInfo;

}
