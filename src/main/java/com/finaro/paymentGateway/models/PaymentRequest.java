package com.finaro.paymentGateway.models;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PaymentRequest {

    private Long invoice;
    private Float amount;
    private String currency;
    private CardHolder cardHolder;
    private Card card ;

}
