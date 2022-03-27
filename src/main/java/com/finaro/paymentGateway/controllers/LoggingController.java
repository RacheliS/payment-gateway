package com.finaro.paymentGateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class LoggingController {
    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    public void info(String info) {
        logger.info(info);
    }
}
