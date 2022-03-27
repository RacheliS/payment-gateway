package com.finaro.paymentGateway.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.finaro.paymentGateway.models.Dao.PaymentDao;
import lombok.*;

import java.io.Serializable;
import java.util.Map;


@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class PaymentResponse implements Serializable {

    private Boolean approved;
    private Map<String, String> errors;
    private PaymentDao paymentDao;

}
