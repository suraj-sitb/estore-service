package com.surajsitb.estore.productsMicroservice.command.aggregator;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.surajsitb.estore.coreService.core.command.CancelProductReservationCommand;
import com.surajsitb.estore.coreService.core.command.ReserveProductCommand;
import com.surajsitb.estore.coreService.core.event.ProductReservationCancelledEvent;
import com.surajsitb.estore.coreService.core.event.ProductReservedEvent;
import com.surajsitb.estore.productsMicroservice.command.model.CreateProductCommand;
import com.surajsitb.estore.productsMicroservice.query.event.model.ProductCreatedEvent;

@Aggregate
public class ProductAggregate {
	// This class will handle all the commands, stores the current state of product and also event sourcing 
	// handlers.
	
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
	public ProductAggregate() {
		// Required by Axon framework to create instance of ProductAggregate class
	}

	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
		// This method will handle & validate createProductCommand
		if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price can't be less than or equal to zero.");
		}
		
		if(createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
			throw new IllegalArgumentException("Title can't be empty.");
		}
		
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
		BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
		
		AggregateLifecycle.apply(productCreatedEvent);
		//On apply method called, it first check for a event sourcing handler method handling the given event.
		
//		if(true) {
//			throw new Exception("Forcefully thrown exception from command. So event will not be applied even-if "
//					+ "apply statement is mentioned above. Here @CommandExecutionException occurs.");
//		}
	}
	
	@CommandHandler
	public void handle(ReserveProductCommand reserveProductCommand) {
		if(quantity < reserveProductCommand.getQuantity()) {
			throw new IllegalStateException("Insufficient amount of product in stock");
		}
		
		ProductReservedEvent productReservedEvent = ProductReservedEvent.builder().
				productId(reserveProductCommand.getProductId()).
				quantity(reserveProductCommand.getQuantity()).
				orderId(reserveProductCommand.getOrderId()).
				userId(reserveProductCommand.getUserId()).build();
		
		AggregateLifecycle.apply(productReservedEvent);
	}
	
	@CommandHandler
	public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
		ProductReservationCancelledEvent productReservationCancelledEvent = 
				ProductReservationCancelledEvent.builder().
				productId(cancelProductReservationCommand.getProductId()).
				orderId(cancelProductReservationCommand.getOrderId()).
				quantity(cancelProductReservationCommand.getQuantity()).
				userId(cancelProductReservationCommand.getUserId()).
				message(cancelProductReservationCommand.getMessage()).
				build();
		
		AggregateLifecycle.apply(productReservationCancelledEvent);
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		// Here, it updates the current product state with provided values and persist in event store.
		// Don't handle any business logic here. just update the state.
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.quantity = productCreatedEvent.getQuantity();
		this.title = productCreatedEvent.getTitle();
	}
	
	@EventSourcingHandler
	public void on(ProductReservedEvent productReservedEvent) {
		this.quantity -= productReservedEvent.getQuantity();
	}
	
	@EventSourcingHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		this.quantity += productReservationCancelledEvent.getQuantity();
	}
}
