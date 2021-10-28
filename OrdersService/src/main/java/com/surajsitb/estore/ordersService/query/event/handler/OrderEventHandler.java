package com.surajsitb.estore.ordersService.query.event.handler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.ordersService.core.entity.OrderEntity;
import com.surajsitb.estore.ordersService.core.repository.OrderRepository;
import com.surajsitb.estore.ordersService.query.event.model.OrderApprovedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderCreatedEvent;
import com.surajsitb.estore.ordersService.query.event.model.OrderRejectedEvent;

@Component
public class OrderEventHandler {

	private final OrderRepository orderRepository;
	
	public OrderEventHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		OrderEntity orderEntity = new OrderEntity();
		
		BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
		
		orderRepository.save(orderEntity);
	}
	
	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderApprovedEvent.getOrderId());
		
		orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
		orderRepository.save(orderEntity);
	}
	
	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		OrderEntity orderEntity = orderRepository.findByOrderId(orderRejectedEvent.getOrderId());
		
		orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
		orderRepository.save(orderEntity);
	}
}
