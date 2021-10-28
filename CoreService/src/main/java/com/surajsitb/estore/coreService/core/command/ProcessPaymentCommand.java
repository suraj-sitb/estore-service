package com.surajsitb.estore.coreService.core.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.surajsitb.estore.coreService.core.model.PaymentDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessPaymentCommand {

	@TargetAggregateIdentifier
	private final String paymentId;
	private final String orderId;
	private final PaymentDetails paymentDetails;
}
