package com.surajsitb.estore.ordersService.command.controller;

import lombok.Value;

@Value
public class RejectOrderCommand {

	private final String orderId;
	private final String message;
}
