package com.finaro.paymentGateway;

import com.finaro.paymentGateway.controllers.LoggingController;
import com.finaro.paymentGateway.controllers.PaymentRestController;
import com.finaro.paymentGateway.enums.Currency;
import com.finaro.paymentGateway.models.Card;
import com.finaro.paymentGateway.models.CardHolder;
import com.finaro.paymentGateway.models.PaymentRequest;
import com.finaro.paymentGateway.models.PaymentResponse;
import com.finaro.paymentGateway.repositories.PaymentDaoRepository;
import com.finaro.paymentGateway.services.Base64EncoderService;
import com.finaro.paymentGateway.services.PaymentService;
import com.finaro.paymentGateway.services.PaymentValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentValidationService paymentValidationService;
    @Mock
    private PaymentDaoRepository paymentRepository;
    @Mock
    private Base64EncoderService base64EncoderService;

    private PaymentRequest paymentRequest = PaymentRequest.builder().invoice(new Long(74455)).amount(32.5)
            .currency(Currency.EUR.toString())
            .cardHolder(CardHolder.builder().name("Racheli").email("s05484388280@gmail.com").build())
            .card(Card.builder().pan("5196081888500645").expiry("0127").cvv("456").build()).build();

    @Test
    public void submitPaymentNotApprovedTest() throws Exception {
        Map<String, String> submitPayment = new HashMap<>();
        submitPayment.put("INVOICE", "REQUIRED");
        ResponseEntity<PaymentResponse> PAYMENT_RESPONSE_ERROR = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PaymentResponse.builder().approved(false).errors(submitPayment).build());
        Mockito.when(paymentValidationService.validRequest(any())).thenReturn(submitPayment);
        ResponseEntity<PaymentResponse> paymentResponseResponseEntity = paymentService.submitPayment(paymentRequest);
        assertEquals(PAYMENT_RESPONSE_ERROR, paymentResponseResponseEntity);
        Mockito.verify(paymentRepository, Mockito.times(0)).save(any());
    }

    @Test
    public void submitPaymentApprovedTest() throws Exception {
        Map<String, String> submitPayment = new HashMap<>();
        ResponseEntity<PaymentResponse> PAYMENT_RESPONSE_ERROR = ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.builder().approved(true).build());
        Mockito.when(paymentValidationService.validRequest(any())).thenReturn(submitPayment);
        Mockito.when(paymentRepository.save(any())).thenReturn(null);
        ResponseEntity<PaymentResponse> paymentResponseResponseEntity = paymentService.submitPayment(paymentRequest);
        assertEquals(PAYMENT_RESPONSE_ERROR, paymentResponseResponseEntity);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(any());
    }


}
