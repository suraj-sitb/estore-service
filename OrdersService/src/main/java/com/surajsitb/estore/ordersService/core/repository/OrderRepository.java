package com.surajsitb.estore.ordersService.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surajsitb.estore.ordersService.core.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

	OrderEntity findByOrderId(String orderId);
}
