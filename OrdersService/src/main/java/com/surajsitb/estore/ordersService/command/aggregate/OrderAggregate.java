package com.surajsitb.estore.ordersService.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.surajsitb.estore.coreService.core.command.ApproveOrderCommand;
import com.surajsitb.estore.ordersService.command.controller.RejectOrderCommand;
import com.surajsitb.estore.ordersService.command.model.CreateOrderCommand;
import com.surajsitb.estore.ordersService.command.model.OrderStatus;
import com.surajsitb.estore.ordersService.query.event.model.OrderApprovedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderCreatedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderRejectedEvent;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;
	
	public OrderAggregate() {

	}

	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		
		AggregateLifecycle.apply(orderCreatedEvent);
	}
	
	@CommandHandler
	public void handle(ApproveOrderCommand approveOrderCommand) {
		OrderApprovedEvent orderApprovedEvent = 
				new OrderApprovedEvent(approveOrderCommand.getOrderId());
		
		AggregateLifecycle.apply(orderApprovedEvent);
	}
	
	@CommandHandler
	public void handle(RejectOrderCommand rejectOrderCommand) {
		OrderRejectedEvent orderRejectedEvent = 
				new OrderRejectedEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getMessage());
		
		AggregateLifecycle.apply(orderRejectedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		this.addressId = orderCreatedEvent.getAddressId();
		this.orderId = orderCreatedEvent.getOrderId();
		this.orderStatus = orderCreatedEvent.getOrderStatus();
		this.productId = orderCreatedEvent.getProductId();
		this.quantity = orderCreatedEvent.getQuantity();
		this.userId = orderCreatedEvent.getUserId();
	}
	
	@EventSourcingHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		this.orderStatus = orderApprovedEvent.getOrderStatus();
	}
	
	@EventSourcingHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		this.orderStatus = orderRejectedEvent.getOrderStatus();
	}
}
