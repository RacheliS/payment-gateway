package com.finaro.paymentGateway;

import com.finaro.paymentGateway.models.Card;
import com.finaro.paymentGateway.models.CardHolder;
import com.finaro.paymentGateway.services.PaymentValidationService;
import org.assertj.core.util.Maps;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest(classes = PaymentValidationService.class)
public class ValidationServiceTest {

    @Autowired
    PaymentValidationService validation;

    @Test
    public void testCreditCardOnSuccess() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        Card card = new Card("5196081888500645", "0127", "147");
        validation.checkValidCard(card, errors);
        Assert.assertEquals("Credit Card validation not working ", Collections.EMPTY_MAP, errors);
    }

    @Test
    public void testCreditCardPanNotValid() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        Card card = new Card("519111888500645", "0127", "147");
        validation.checkValidCard(card, errors);
        Assert.assertNotEquals("Credit Card not Valid", Collections.EMPTY_MAP, errors);
    }

    @Test
    public void testCreditCardExpiryDatePass() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        Card card = new Card("5196081888500645", "0120", "147");
        validation.checkValidCard(card, errors);
        Assert.assertNotEquals("Card expiry date pass", Collections.EMPTY_MAP, errors);
    }

    @Test
    public void testValidationCardHolderSuccess() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        CardHolder cardHolder = new CardHolder("Racheli", "s0548438280@gmail.com");
        validation.checkValidCardholder(cardHolder, errors);
        Assert.assertEquals("Email validation not working", Collections.EMPTY_MAP, errors);
    }

    @Test
    public void testValidationCardHolderEmailNotValid() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        CardHolder cardHolder = new CardHolder("Racheli", "s0548438280@gmail");
        validation.checkValidCardholder(cardHolder, errors);
        Assert.assertNotEquals("Email validation not working", Collections.EMPTY_MAP, errors);
    }



}
