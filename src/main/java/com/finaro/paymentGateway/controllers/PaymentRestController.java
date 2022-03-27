package com.finaro.paymentGateway.controllers;


import com.finaro.paymentGateway.models.PaymentRequest;
import com.finaro.paymentGateway.models.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.finaro.paymentGateway.services.PaymentService;

@RestController
@RequestMapping("/payments/services")
public class PaymentRestController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/submit")
    public ResponseEntity<PaymentResponse> submitPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.submitPayment(paymentRequest);
    }

    @GetMapping("/getPayment/{invoice}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable(value = "invoice") Long invoice) {
        return paymentService.getPayment(invoice);
    }

    @GetMapping("/getAllPayments")
    public ResponseEntity<String> getAllPayments() {
        return paymentService.printAllPayments();
    }
}
