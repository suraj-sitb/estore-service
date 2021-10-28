package com.surajsitb.estore.productsMicroservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surajsitb.estore.productsMicroservice.core.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

	ProductEntity findByProductIdOrTitle(String productId, String title);
	ProductEntity findByProductId(String productId);
}
