package com.finaro.paymentGateway.services;

import com.finaro.paymentGateway.models.Card;
import com.finaro.paymentGateway.models.CardHolder;
import com.finaro.paymentGateway.models.PaymentRequest;
import com.finaro.paymentGateway.repositories.PaymentDaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.finaro.paymentGateway.utils.ValidationUtils;


import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentValidationService {

    @Autowired
    PaymentDaoRepository paymentRepository;

    private final String REQUIRED_FILED = "Required filed is empty";

    public Map<String, String> validRequest(PaymentRequest paymentRequest) {
        Map<String, String> errors = new HashMap<String, String>();
        checkValidInvoice(paymentRequest.getInvoice(), errors);
        checkValidAmount(paymentRequest.getAmount(), errors);
        checkValidCurrency(paymentRequest.getCurrency(), errors);
        checkValidCardholder(paymentRequest.getCardHolder(), errors);
        checkValidCard(paymentRequest.getCard(), errors);
        return errors;
    }

    public void checkValidInvoice(Long invoice, Map<String, String> errors) {
        if (invoice == null) {
            errors.put("Invoice", REQUIRED_FILED);
        }
        if (paymentRepository.existsById(invoice)) {
            errors.put("Invoice", "Payment already existing, invoice is unique filed");
        }
    }

    public void checkValidAmount(Double amount, Map<String, String> errors) {
        if (amount == null || amount == 0) {
            errors.put("Amount", REQUIRED_FILED);
        }
        if (amount != null && amount < 0) {
            errors.put("Amount", "Amount should be a positive value");
        }
    }

    public void checkValidCurrency(String currency, Map<String, String> errors) {
        if (!StringUtils.hasLength(currency)) {
            errors.put("Currency", REQUIRED_FILED);
        }
        if (!ValidationUtils.isSupportedCurrency(currency)) {
            errors.put("Currency", " Currency is not supported value");
        }
    }

    public void checkValidCardholder(CardHolder cardholder, Map<String, String> errors) {
        if (!StringUtils.hasLength(cardholder.getName())) {
            errors.put("CardHolder.name", REQUIRED_FILED);
        }
        if (!StringUtils.hasLength(cardholder.getEmail())) {
            errors.put("CardHolder.name", REQUIRED_FILED);
        }
        if (!ValidationUtils.isEmailValid(cardholder.getEmail())) {
            errors.put("CardHolder.email", "Email should be a valid Email address");
        }
    }

    public void checkValidCard(Card card, Map<String, String> errors) {
        if (!StringUtils.hasLength(card.getPan())) {
            errors.put("Card.pan", REQUIRED_FILED);
        }
        if (!StringUtils.hasLength(card.getExpiry())) {
            errors.put("Card.expiry", REQUIRED_FILED);
        }
        if (!StringUtils.hasLength(card.getCvv())) {
            errors.put("Card.cvv", REQUIRED_FILED);
        }
        if (!ValidationUtils.isCreditCardValid(card.getPan())) {
            errors.put("Card.pan", "Card number should be a valid number");
        }
        if (!ValidationUtils.isExpiryDateValid(card.getExpiry())) {
            errors.put("Card.expiry", "Card expiration date has passed");
        }
        if (card.getExpiry() != null && card.getExpiry().length() != 4) {
            errors.put("card.expiry", "Expiry date should be provide with 4 digits value ( ex : 01/22");
        }

    }


}
