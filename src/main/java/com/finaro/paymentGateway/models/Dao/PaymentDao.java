package com.finaro.paymentGateway.models;

import lombok.*;

import javax.persistence.*;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Payments", indexes = {@Index(columnList = "invoice")})
public class PaymentDao {

    @Id
    private Long invoice;
    private Float amount;
    private String currency;
    private String name;
    private String email;
    private String pan;
    private String expiry;

}
