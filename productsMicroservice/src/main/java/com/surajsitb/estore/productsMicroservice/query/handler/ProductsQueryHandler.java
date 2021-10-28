package com.surajsitb.estore.productsMicroservice.query.handler;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.productsMicroservice.core.entity.ProductEntity;
import com.surajsitb.estore.productsMicroservice.core.repository.ProductRepository;
import com.surajsitb.estore.productsMicroservice.query.model.FindProductsQuery;
import com.surajsitb.estore.productsMicroservice.response.model.GetProductResponseModel;

@Component
public class ProductsQueryHandler {

	private final ProductRepository productRepository;

	public ProductsQueryHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@QueryHandler
	public List<GetProductResponseModel> findProducts(FindProductsQuery findProductsQuery) {
		List<GetProductResponseModel> products = new ArrayList<>();
		
		List<ProductEntity> productsStored = productRepository.findAll();
		for(ProductEntity product : productsStored) {
			GetProductResponseModel response = new GetProductResponseModel();
			BeanUtils.copyProperties(product, response);
			products.add(response);
		}
		
		return products;
	}
}
