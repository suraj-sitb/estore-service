package com.surajsitb.estore.ordersService.request.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateOrderRequestModel {

	@NotBlank(message = "Product id can't be blank or empty.")
	private String productId;
	
	@Min(value = 1, message = "Quantity should be greater than 1.")
	private Integer quantity;
	
	@NotBlank(message = "Address id can't be blank or empty.")
	private String addressId;
}
