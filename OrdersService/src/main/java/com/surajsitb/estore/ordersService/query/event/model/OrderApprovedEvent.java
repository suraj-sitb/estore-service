package com.surajsitb.estore.ordersService.query.event.model;

import com.surajsitb.estore.ordersService.command.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.CREATED;
}
