package com.surajsitb.estore.paymentsService.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {

	@Id
	private String paymentId;
	@Column
	public String orderId;
}
