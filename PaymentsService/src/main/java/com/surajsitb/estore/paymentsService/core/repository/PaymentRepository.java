package com.surajsitb.estore.paymentsService.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surajsitb.estore.paymentsService.core.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

}
