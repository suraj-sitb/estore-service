package com.surajsitb.estore.productsMicroservice.response.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GetProductResponseModel {

	private String title;
	private BigDecimal price;
	private Integer quantity;
}
