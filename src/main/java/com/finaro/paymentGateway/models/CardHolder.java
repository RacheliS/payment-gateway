package com.finaro.paymentGateway.models;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CardHolder implements Serializable {

    private String name;
    private String email;
}
