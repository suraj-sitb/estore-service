package com.surajsitb.estore.productsMicroservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surajsitb.estore.productsMicroservice.core.entity.ProductLookupEntity;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

	ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
