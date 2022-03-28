package com.finaro.paymentGateway.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.finaro.paymentGateway.models.Dao.PaymentDao;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    Base64EncoderService base64EncoderService;

    @Autowired
    MaskService maskService;

    private static final Logger LOG = LoggerFactory.getLogger(AuditService.class);

    @Value("${audit.file.location}")
    private String fileName;
    private FileOutputStream fileOutputStream;
    private Gson gson;

    @PostConstruct
    public void init() {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("File not found or cannot be created", e);
        } catch (IOException e) {
            LOG.error("File not found or cannot be created", e);
        }
    }

    public ResponseEntity<String> exportPayments(List<PaymentDao> paymentDaoList) {
        if (paymentDaoList.isEmpty())
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found entities to print" + fileName);   
        try {
            for (PaymentDao paymentDao : paymentDaoList) {
                exportPayments(maskService.maskData(base64EncoderService.decrypt(paymentDao)));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail writing to file");     
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Success writing to file on path" + fileName);   
    }

    public void exportPayments(PaymentDao paymentDao) throws Exception {
        gson = new Gson();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            fileOutputStream.write((gson.toJson(paymentDao) + "," + "\n").getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            LOG.error("Payment not write to file", e);
            throw new Exception("Payment not write to file");
        }
    }
}
