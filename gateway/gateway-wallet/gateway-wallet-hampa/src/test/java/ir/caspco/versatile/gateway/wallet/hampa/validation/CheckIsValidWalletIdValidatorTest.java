package ir.caspco.versatile.gateway.wallet.hampa.validation;

import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.context.validation.exception.ValidationException;
import ir.caspco.versatile.gateway.wallet.hampa.client.WalletClient;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.validation.classes.WalletIdClass;
import ir.caspco.versatile.rest.client.configuration.RestClientBeanConfiguration;
import ir.caspco.versatile.validation.DefaultValidator;
import ir.caspco.versatile.validation.configuration.ValidatorConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RestClientBeanConfiguration.class,
        RestTemplate.class,
        WalletClient.class,
        ValidatorConfiguration.class,
        DefaultValidator.class,
        WalletIdClass.class,
        ApplicationContextUtil.class
})
@TestPropertySource(locations = "classpath:rest-client.properties")
class CheckIsValidWalletIdValidatorTest {

    @Autowired
    WalletClient walletClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DValidator dValidator;

    @Autowired
    WalletIdClass walletIdClass;

    private final String serverAddress = "http://localhost";

    private MockRestServiceServer mockServer;


    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void isValid() throws NoSuchMethodException {

        final String fullPath = serverAddress + "/wallet/getWalletInfo";

        ResultVO result = ResultVO.builder()
                .error(true)
                .build();

        mockServer.expect(requestTo(fullPath))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(Shift.just(result).toJson(), MediaType.APPLICATION_JSON));

        Method type = WalletIdClass.class.getMethod("walletId", String.class);

        try {

            dValidator.validateParameters(walletIdClass, type, "ce3d042b-7de9-4e5f-a780-6c935db72035");

            fail();

        } catch (Exception e) {

            assertTrue(e instanceof ValidationException);

            equals((ValidationException) e);

        }

    }

    @Test
    void isValidCorrect() throws NoSuchMethodException {

        final String fullPath = serverAddress + "/wallet/getWalletInfo";

        ResultVO result = ResultVO.builder()
                .error(false)
                .build();

        mockServer.expect(requestTo(fullPath))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(Shift.just(result).toJson(), MediaType.APPLICATION_JSON));

        Method type = WalletIdClass.class.getMethod("walletId", String.class);


        dValidator.validateParameters(walletIdClass, type, "ce3d042b-7de9-4e5f-a780-6c935db72035");

    }

    private void equals(ValidationException e) {
        Map<Integer, Map<String, List<String>>> nodeViolations = e.nodeViolations();

        String actual = nodeViolations.get(-1).get("ir.caspco.versatile.gateway.wallet.hampa.validation.classes.WalletIdClass.walletId.walletId").get(0);

        assertEquals("ir.caspco.versatile.common.validation.annotations.IsValidWalletId.message", actual);
    }

}