package com.devopsbuddy.test.integration;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.service.StripeService;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by tedonema on 08/05/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsbuddyApplication.class)
public class StripeIntegrationTest {

    /** The application logger. */
    private static final Logger LOG = LoggerFactory.getLogger(StripeIntegrationTest.class);

    @Autowired
    private StripeService stripeService;

    @Autowired
    private String stripeKey;

    @Before
    public void init() {
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }

    @Test
    public void testRetrieveAllPlans() throws Exception {

        Map<String, Object> planParams = new HashMap<String, Object>();
        planParams.put("limit", 3);

        PlanCollection planCollection = Plan.list(planParams);
        planCollection.autoPagingIterable().forEach(p -> LOG.info("Plan: {}", p.getId()));
        Assert.assertTrue(planCollection.getData().size() == 1);

    }

    @Test
    public void createStripeCustomer() throws Exception {

        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", "4242424242424242");
        cardParams.put("exp_month", 1);
        cardParams.put("exp_year", 2017);
        cardParams.put("cvc", "314");
        tokenParams.put("card", cardParams);

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Customer for test@example.com");

        String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
        assertThat(stripeCustomerId, is(notNullValue()));

        Customer cu = Customer.retrieve(stripeCustomerId);
        cu.delete();

    }
}
