package com.surajsitb.estore.productsMicroservice.request.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateProductRequestModel {
	
	@NotBlank(message = "Product title can't be empty")
	private String title;
	
	@Min(value = 1, message = "Product price must be greater than one")
	private BigDecimal price;
	
	@Min(value = 1, message = "Product quantity must be greater than one")
	private Integer quantity;
}
