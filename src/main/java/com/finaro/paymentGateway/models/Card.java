package com.finaro.paymentGateway.models;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Card implements Serializable {

    private String pan;
    private String expiry;
    private String cvv;

}
