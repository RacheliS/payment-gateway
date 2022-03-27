package com.finaro.paymentGateway.models;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private Long invoice;
    private Double amount;
    private String currency;
    private CardHolder cardHolder;
    private Card card;

}
