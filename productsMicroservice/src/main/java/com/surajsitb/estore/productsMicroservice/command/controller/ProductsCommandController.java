package com.surajsitb.estore.productsMicroservice.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.surajsitb.estore.productsMicroservice.request.model.CreateProductRequestModel;
import com.surajsitb.estore.productsMicroservice.command.model.CreateProductCommand;

@RestController
@RequestMapping(path = "/products")
public class ProductsCommandController {
	
	private final CommandGateway commandGateway;
	
	@Autowired
	public ProductsCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createProduct(@Valid @RequestBody CreateProductRequestModel createProductRequestModel) {
		//Command object got created and user i/p values are passed to it 
		CreateProductCommand createProduct = CreateProductCommand.builder()
				.price(createProductRequestModel.getPrice())
				.quantity(createProductRequestModel.getQuantity())
				.title(createProductRequestModel.getTitle())
				.productId(UUID.randomUUID().toString())
				.build();
		
		String returnValue;
		
		returnValue = commandGateway.sendAndWait(createProduct);
//		try {
//			returnValue = commandGateway.sendAndWait(createProduct);
//		} catch (Exception e) {
//			returnValue = e.getLocalizedMessage();
//		}
		return returnValue;
	}
}