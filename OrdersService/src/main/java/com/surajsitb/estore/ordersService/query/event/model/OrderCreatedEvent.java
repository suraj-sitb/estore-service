package com.surajsitb.estore.ordersService.query.event.model;

import com.surajsitb.estore.ordersService.command.model.OrderStatus;

import lombok.Data;

@Data
public class OrderCreatedEvent {

	private String orderId;
	private String productId;
	private String userId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;
}
