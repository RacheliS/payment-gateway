package com.finaro.paymentGateway.services;

import com.finaro.paymentGateway.controllers.LoggingController;
import com.finaro.paymentGateway.models.PaymentDao;
import com.finaro.paymentGateway.models.PaymentRequest;
import com.finaro.paymentGateway.models.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.finaro.paymentGateway.repositories.PaymentDaoRepository;


import java.util.Map;
import java.util.Optional;

@Service
public class PaymentControllerService {

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

    @Autowired
    LoggingController log;

    public ResponseEntity<PaymentResponse> submitPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Submit payment service [ Request payment :" + paymentRequest.toString() + "]");
        PaymentResponse paymentResponse = new PaymentResponse();
        Map<String, String> errorsByFiledName = paymentValidationService.validRequest(paymentRequest);
        if (!errorsByFiledName.isEmpty()) {
            paymentResponse.setApproved(false);
            paymentResponse.setErrors(errorsByFiledName);
            log.info("Submit payment fail , payment not approved [ Errors :" + paymentResponse.toString() + "]");
            return new ResponseEntity<>(paymentResponse, HttpStatus.BAD_REQUEST);
        } else {
            try {
                if (!paymentRepository.existsById(paymentRequest.getInvoice())) {
                    paymentResponse.setApproved(true);
                    PaymentDao paymentDao = new PaymentDao(paymentRequest.getInvoice(), paymentRequest.getAmount(), paymentRequest.getCurrency(),
                            paymentRequest.getCardHolder().getName(), paymentRequest.getCardHolder().getEmail(),
                            paymentRequest.getCard().getPan(), paymentRequest.getCard().getExpiry());
                    paymentDao = paymentRepository.save(base64EncoderService.encrypt(paymentDao));
                    log.info("Submit payment success [ Invoice :" + paymentRequest.getInvoice().toString() + "]");
                    return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
                } else {
                    errorsByFiledName.put("Invoice", "Payment already existing, invoice is unique filed");
                    paymentResponse.setApproved(false);
                    paymentResponse.setErrors(errorsByFiledName);
                    return new ResponseEntity<>(paymentResponse, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                log.info("Submit payment fail [ Invoice :" + paymentRequest.getInvoice().toString() + "]");
                paymentResponse.setApproved(false);
                return new ResponseEntity<>(paymentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<String> getPayment(Long invoice) {
        log.info("Get payment service [ Invoice :" + invoice + "]");
        PaymentResponse paymentResponse = new PaymentResponse();
        if (invoice == null) {
            return new ResponseEntity<>("Invoice not valid", HttpStatus.BAD_REQUEST);
        }
        Optional<PaymentDao> paymentDao = paymentRepository.findById(invoice);
        if (paymentDao.isPresent()) {
            PaymentDao decrypt = base64EncoderService.decrypt(paymentDao.get());
            return new ResponseEntity<>(maskService.maskData(decrypt).toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invoice Payment Not exist", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> printAllPayments() {
        log.info("Print all Payments :");
        return auditService.exportPayments(paymentRepository.findAll());
    }

}
