
// Author :Rachel Amrusi
// Date :27.03.2022

package com.finaro.paymentGateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan({"com.finaro.*"})
public class PaymentGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentGatewayApplication.class, args);

    }
}
