package com.surajsitb.estore.productsMicroservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.surajsitb.estore.productsMicroservice.query.model.FindProductsQuery;
import com.surajsitb.estore.productsMicroservice.response.model.GetProductResponseModel;

@RestController
@RequestMapping(path = "/products")
public class ProductsQueryController {

	private final QueryGateway queryGateway;
	
	public ProductsQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<GetProductResponseModel> getProducts() {
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<GetProductResponseModel> products = queryGateway.query(findProductsQuery, 
				ResponseTypes.multipleInstancesOf(GetProductResponseModel.class)).join();
		
		return products;
	}
}
