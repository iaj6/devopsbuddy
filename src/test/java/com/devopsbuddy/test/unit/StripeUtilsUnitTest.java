package com.devopsbuddy.test.unit;

import com.devopsbuddy.utils.StripeUtils;
import com.devopsbuddy.web.domain.frontend.ProAccountPayload;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by tedonema on 03/01/2016.
 */
public class StripeUtilsUnitTest {

    private PodamFactory podam;

    @Before
    public void init() {
        podam = new PodamFactoryImpl();
    }

    @Test
    public void createStripeTokenParamsFromUserPayload() {

        ProAccountPayload payload = podam.manufacturePojo(ProAccountPayload.class);
        String cardNumber = "4242424242424242";
        payload.setCardNumber(cardNumber);
        String cardCode = "314";
        payload.setCardCode(cardCode);
        String cardMonth = "1";
        payload.setCardMonth(cardMonth);
        String cardYear = "2017";
        payload.setCardYear(cardYear);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
        assertThat(cardNumber, is(cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY)));
        assertThat(cardMonth, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_MONTH_KEY))));
        assertThat(cardYear, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_YEAR_KEY))));
        assertThat(cardCode, is(cardParams.get(StripeUtils.STRIPE_CVC_KEY)));
    }
}
