package com.surajsitb.estore.productsMicroservice.query.event.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductCreatedEvent {

	//Here we can decide which information is required to persist in database.
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
