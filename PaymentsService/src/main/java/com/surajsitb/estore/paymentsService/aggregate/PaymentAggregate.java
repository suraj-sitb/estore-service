package com.surajsitb.estore.paymentsService.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.surajsitb.estore.coreService.core.command.ProcessPaymentCommand;
import com.surajsitb.estore.coreService.core.event.PaymentProcessedEvent;

@Aggregate
public class PaymentAggregate {

	private String orderId;
	@AggregateIdentifier
	private String paymentId;
	
	public PaymentAggregate() {

	}

	@CommandHandler
	public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
		if (processPaymentCommand.getOrderId() == null || processPaymentCommand.getOrderId().isBlank()
				|| processPaymentCommand.getPaymentId() == null || processPaymentCommand.getPaymentId().isBlank()) {
			throw new IllegalArgumentException("Payment can't be processed.");
		}
		
		PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder().
				orderId(processPaymentCommand.getOrderId()).
				paymentId(processPaymentCommand.getPaymentId()).build();
		
		AggregateLifecycle.apply(paymentProcessedEvent);
	}
	
	@EventSourcingHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		this.orderId = paymentProcessedEvent.getOrderId();
		this.paymentId = paymentProcessedEvent.getPaymentId();
	}
}
