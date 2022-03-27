package com.finaro.paymentGateway.services;

import com.finaro.paymentGateway.models.PaymentDao;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64EncoderService {

    public PaymentDao encrypt(PaymentDao payment) {
        PaymentDao encryptPayment = new PaymentDao();
        setDefaultValues(payment, encryptPayment);
        encryptPayment.setName(encryptBase64(payment.getName()));
        encryptPayment.setPan(encryptBase64(payment.getPan()));
        encryptPayment.setExpiry(encryptBase64(payment.getExpiry()));
        return encryptPayment;
    }

    public PaymentDao decrypt(PaymentDao payment) {
        PaymentDao decryptPayment = new PaymentDao();
        setDefaultValues(payment, decryptPayment);
        decryptPayment.setName(decryptBase64(payment.getName()));
        decryptPayment.setPan(decryptBase64(payment.getPan()));
        decryptPayment.setExpiry(decryptBase64(payment.getExpiry()));
        return decryptPayment;
    }

    private void setDefaultValues(PaymentDao payment, PaymentDao paymentDao) {
        paymentDao.setInvoice(payment.getInvoice());
        paymentDao.setEmail(payment.getEmail());
        paymentDao.setCurrency(payment.getCurrency());
        paymentDao.setAmount(payment.getAmount());
    }

    private String encryptBase64(String source) {
        return new String(Base64.getEncoder().encode(source.getBytes()));
    }

    private String decryptBase64(String source) {
        return new String(Base64.getDecoder().decode(source.getBytes()));
    }
}
