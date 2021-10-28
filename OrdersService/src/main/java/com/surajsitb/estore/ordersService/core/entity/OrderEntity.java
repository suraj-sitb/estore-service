package com.surajsitb.estore.ordersService.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.surajsitb.estore.ordersService.command.model.OrderStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

	@Id
	@Column(unique = true)
	public String orderId;
	private String productId;
	private String userId;
	private int quantity;
	private String addressId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
}
