package com.surajsitb.estore.productsMicroservice.command.model;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateProductCommand {

	@TargetAggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
