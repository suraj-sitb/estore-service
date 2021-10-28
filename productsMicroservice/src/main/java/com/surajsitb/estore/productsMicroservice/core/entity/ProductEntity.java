package com.surajsitb.estore.productsMicroservice.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class ProductEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2622281095912539472L;

	@Id
	@Column(unique = true)
	private String productId;
	
	@Column(unique = true)
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
