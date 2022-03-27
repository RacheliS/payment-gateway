package com.finaro.paymentGateway.services;

import com.finaro.paymentGateway.models.PaymentDao;
import org.springframework.stereotype.Service;

@Service
public class MaskService {

    private final String MASK_CHAR = "*";

    public PaymentDao maskData(PaymentDao payment) {
        PaymentDao maskPayment = new PaymentDao();
        maskPayment.setInvoice(payment.getInvoice());
        maskPayment.setEmail(payment.getEmail());
        maskPayment.setCurrency(payment.getCurrency());
        maskPayment.setAmount(payment.getAmount());
        maskPayment.setName(mask(payment.getName(), payment.getName().length()));
        maskPayment.setPan(mask(payment.getPan(), payment.getPan().length() - 4));
        maskPayment.setExpiry(mask(payment.getExpiry(), payment.getExpiry().length()));
        return maskPayment;
    }

    private String mask(String s, int len) {
        if (len <= 0) {
            return s;
        }
        len = Math.min(len, s.length());
        StringBuilder sb = new StringBuilder();
        sb.append(MASK_CHAR.repeat(len));
        sb.append(s.substring(len));
        return sb.toString();
    }


}
