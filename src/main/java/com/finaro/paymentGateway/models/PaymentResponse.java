package com.finaro.paymentGateway.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Map;


@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse implements Serializable {

    private Boolean approved;
    private Map<String, String> errors;

}
