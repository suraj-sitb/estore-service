package com.surajsitb.estore.productsMicroservice.core.errorHandling.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

	private Date timeStamp;
	private String message;
}
