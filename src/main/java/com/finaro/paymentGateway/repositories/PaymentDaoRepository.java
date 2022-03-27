package com.finaro.paymentGateway.repositories;

import com.finaro.paymentGateway.models.Dao.PaymentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDaoRepository extends JpaRepository<PaymentDao, Long> {

    List<PaymentDao> findByInvoice(String invoice);

}
