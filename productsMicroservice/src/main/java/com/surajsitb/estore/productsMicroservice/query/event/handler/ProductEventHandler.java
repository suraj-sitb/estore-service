package com.surajsitb.estore.productsMicroservice.query.event.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.coreService.core.event.ProductReservationCancelledEvent;
import com.surajsitb.estore.coreService.core.event.ProductReservedEvent;
import com.surajsitb.estore.productsMicroservice.core.entity.ProductEntity;
import com.surajsitb.estore.productsMicroservice.core.repository.ProductRepository;
import com.surajsitb.estore.productsMicroservice.query.event.model.ProductCreatedEvent;

@Component
@ProcessingGroup(value = "product-group")
public class ProductEventHandler {

	// This will handle event published by product aggregate and persist it into database.
	private final ProductRepository productRepository;
	
	public ProductEventHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException ex) {
		//log error message
	}
	
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception ex) throws Exception {
		throw ex;
	}

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		
		productRepository.save(productEntity);
		
		if(true) {
			throw new Exception("Forcefully thrown exception from event Handler. So exception will occur but "
					+ "controller will have no idea about it. It will follow normal execution."
					+ "Can use ListenerInvocationErrorHandler to propagate the error to controller."
					+ " So no evnts will be stored in event store and also not persist to db.");
		}
		
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		productRepository.save(productEntity);
	}
	
	@EventHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		ProductEntity productEntity = 
				productRepository.findByProductId(productReservationCancelledEvent.getProductId());
		
		productEntity.setQuantity(productEntity.getQuantity() + productReservationCancelledEvent.getQuantity());
		productRepository.save(productEntity);
	}
}
