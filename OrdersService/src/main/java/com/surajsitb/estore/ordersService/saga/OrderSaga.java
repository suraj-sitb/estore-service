package com.surajsitb.estore.ordersService.saga;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surajsitb.estore.coreService.core.command.ApproveOrderCommand;
import com.surajsitb.estore.coreService.core.command.CancelProductReservationCommand;
import com.surajsitb.estore.coreService.core.command.ProcessPaymentCommand;
import com.surajsitb.estore.coreService.core.command.ReserveProductCommand;
import com.surajsitb.estore.coreService.core.event.PaymentProcessedEvent;
import com.surajsitb.estore.coreService.core.event.ProductReservationCancelledEvent;
import com.surajsitb.estore.coreService.core.event.ProductReservedEvent;
import com.surajsitb.estore.coreService.core.model.User;
import com.surajsitb.estore.coreService.core.query.FetchUserPaymentDetailsQuery;
import com.surajsitb.estore.ordersService.command.controller.RejectOrderCommand;
import com.surajsitb.estore.ordersService.query.event.model.OrderApprovedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderCreatedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderRejectedEvent;

@Saga
public class OrderSaga {

	private final Logger logger = LoggerFactory.getLogger(OrderSaga.class);
	
	private transient CommandGateway commandGateway;
	private transient QueryGateway queryGateway;

	public OrderSaga(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder().
				productId(orderCreatedEvent.getProductId()).
				quantity(orderCreatedEvent.getQuantity()).
				orderId(orderCreatedEvent.getOrderId()).
				userId(orderCreatedEvent.getUserId()).build();
		
		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				if(commandResultMessage.isExceptional()) {
					//add compensating transactions.
				}
			}
		});
	}
	
	//after reserving product, payment details will be queried from user-service. ProcessPaymentCommand will 
	// be called.
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		FetchUserPaymentDetailsQuery query = 
				new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
		
		User userResponse = null;
		
		try {
			userResponse = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
		} catch (Exception e) {
			// Do Compensating transactions
			cancelProductReservationHandler(productReservedEvent, "Failed to get user details.");
			return;
		}
		
		if(userResponse == null) {
			//Do Compensating transactions
			cancelProductReservationHandler(productReservedEvent, "User doesn't have any payment details.");
		}
		
		logger.info("Sucessfully received payment details for user:" + userResponse.getFirstName());
		
		ProcessPaymentCommand paymentCommand = ProcessPaymentCommand.builder().
				orderId(productReservedEvent.getOrderId()).
				paymentDetails(userResponse.getPaymentDetails()).
				paymentId(UUID.randomUUID().toString()).build();
		
		String result = null;
		
		try {
			result = commandGateway.sendAndWait(paymentCommand, 10, TimeUnit.SECONDS);
		} catch (Exception e) {
			//do compensation transactions
			cancelProductReservationHandler(productReservedEvent, e.getMessage());
			logger.error(e.getMessage());
		}
		
		if(result == null) {
			logger.info("Payment processing failed. Initiate compensating transactions!!!");
		}
	}
	
	private void cancelProductReservationHandler(ProductReservedEvent productReservedEvent,
			String message) {
		CancelProductReservationCommand cancelProductReservationCommand = 
				CancelProductReservationCommand.builder().
				productId(productReservedEvent.getProductId()).
				orderId(productReservedEvent.getOrderId()).
				quantity(productReservedEvent.getQuantity()).
				userId(productReservedEvent.getUserId()).
				message(message).
				build();
		
		commandGateway.send(cancelProductReservationCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		ApproveOrderCommand approveOrderCommand = 
				new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		
		commandGateway.send(approveOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		logger.info("Order is approved.");
//		SagaLifecycle.end();
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
		RejectOrderCommand rejectOrderCommand = 
				new RejectOrderCommand(productReservationCancelledEvent.getOrderId(), 
						productReservationCancelledEvent.getMessage());
		
		commandGateway.send(rejectOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		logger.info("Order is rejected successfully.");
//		SagaLifecycle.end();
	}
}
