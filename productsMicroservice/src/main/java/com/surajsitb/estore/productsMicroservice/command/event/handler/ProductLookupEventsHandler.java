package com.surajsitb.estore.productsMicroservice.command.event.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.productsMicroservice.core.entity.ProductLookupEntity;
import com.surajsitb.estore.productsMicroservice.core.repository.ProductLookupRepository;
import com.surajsitb.estore.productsMicroservice.query.event.model.ProductCreatedEvent;

@Component
@ProcessingGroup(value = "product-group")
public class ProductLookupEventsHandler {

	private final ProductLookupRepository productLookupRepository;
	
	public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		ProductLookupEntity productLookupEntity = new ProductLookupEntity(productCreatedEvent.getProductId(), 
				productCreatedEvent.getTitle());
		
		productLookupRepository.save(productLookupEntity);
	}
}
