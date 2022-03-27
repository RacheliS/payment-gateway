package com.finaro.paymentGateway.services;

import com.finaro.paymentGateway.controllers.LoggingController;
import com.finaro.paymentGateway.models.Dao.PaymentDao;
import com.finaro.paymentGateway.models.PaymentRequest;
import com.finaro.paymentGateway.models.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.finaro.paymentGateway.repositories.PaymentDaoRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentValidationService paymentValidationService;

    @Autowired
    Base64EncoderService base64EncoderService;

    @Autowired
    MaskService maskService;

    @Autowired
    AuditService auditService;

    @Autowired
    PaymentDaoRepository paymentRepository;

    Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    public ResponseEntity<PaymentResponse> submitPayment(@RequestBody PaymentRequest paymentRequest) {
        LOG.info("Submit payment service [ Request payment :" + paymentRequest.toString() + "]");
        Map<String, String> errorsByFiledName = paymentValidationService.validRequest(paymentRequest);
        PaymentResponse paymentResponse = PaymentResponse.builder().approved(false).build();
        if (!errorsByFiledName.isEmpty()) {
            return handleErrorsMsg(paymentResponse, errorsByFiledName);
        } else {
            return savePayment(paymentRequest, paymentResponse);
        }
    }

    private ResponseEntity<PaymentResponse> savePayment(PaymentRequest paymentRequest, PaymentResponse paymentResponse) {
        try {
            paymentResponse.setApproved(true);
            paymentRepository.save(base64EncoderService.encrypt(convertRequest(paymentRequest)));
            LOG.info("Submit payment success [ Invoice :" + paymentRequest.getInvoice().toString() + "]");
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
        } catch (Exception e) {
            LOG.error("Submit payment failed [ Invoice :" + paymentRequest.getInvoice().toString() + "][ ex = " +
                    e.getMessage() + "]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(paymentResponse);
        }
    }

    private PaymentDao convertRequest(PaymentRequest paymentRequest) {
        return PaymentDao.builder().invoice(paymentRequest.getInvoice())
                .amount(paymentRequest.getAmount())
                .currency(paymentRequest.getCurrency())
                .name(paymentRequest.getCardHolder().getName())
                .email(paymentRequest.getCardHolder().getEmail())
                .pan(paymentRequest.getCard().getPan())
                .expiry(paymentRequest.getCard().getExpiry()).build();
    }

    private ResponseEntity<PaymentResponse> handleErrorsMsg(PaymentResponse paymentResponse, Map<String, String> errorsByFiledName) {
        paymentResponse.setErrors(errorsByFiledName);
        LOG.info("Submit payment failed , payment not approved [ Errors :" + paymentResponse.toString() + "]");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResponse);
    }

    public ResponseEntity<PaymentResponse> getPayment(Long invoice) {
        LOG.info("Get payment service [ Invoice :" + invoice + "]");
        PaymentResponse paymentResponse = PaymentResponse.builder().build();
        Optional<PaymentDao> paymentDao = paymentRepository.findById(invoice);
        if (paymentDao.isPresent()) {
            PaymentDao decrypt = base64EncoderService.decrypt(paymentDao.get());
            paymentResponse.setPaymentDao(maskService.maskData(decrypt));
            return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(paymentResponse);
        }
    }

    public ResponseEntity<String> printAllPayments() {
        LOG.info("Print all Payments :");
        return auditService.exportPayments(paymentRepository.findAll());
    }

}
