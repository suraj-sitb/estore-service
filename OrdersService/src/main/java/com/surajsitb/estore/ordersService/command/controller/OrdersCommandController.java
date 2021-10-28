package com.surajsitb.estore.ordersService.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.surajsitb.estore.ordersService.command.model.CreateOrderCommand;
import com.surajsitb.estore.ordersService.command.model.OrderStatus;
import com.surajsitb.estore.ordersService.request.model.CreateOrderRequestModel;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

	private final CommandGateway commandGateway;
	
	public OrdersCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createOrder(@Valid @RequestBody CreateOrderRequestModel createOrderRequestModel) {
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().
				productId(createOrderRequestModel.getProductId()).
				quantity(createOrderRequestModel.getQuantity()).
				addressId(createOrderRequestModel.getAddressId()).
				orderId(UUID.randomUUID().toString()).
				orderStatus(OrderStatus.CREATED).build();
		
		String returnValue = commandGateway.sendAndWait(createOrderCommand);
		
		return returnValue;
	}
}
