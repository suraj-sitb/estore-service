package com.surajsitb.estore.usersService.query.handler;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.coreService.core.model.PaymentDetails;
import com.surajsitb.estore.coreService.core.model.User;
import com.surajsitb.estore.coreService.core.query.FetchUserPaymentDetailsQuery;

@Component
public class UserQueryHandler {

	@QueryHandler
	public User fetchUserPaymentDetails(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery) {
		PaymentDetails paymentDetails = PaymentDetails.builder()
				.cardNumber("123Card")
				.cvv("123")
				.name("SURAJ KUMAR")
				.validUntilMonth(12)
				.validUntilYear(2030)
				.build();

		User user = User.builder()
				.firstName("Suraj")
				.lastName("Kumar")
				.userId(fetchUserPaymentDetailsQuery.getUserId())
				.paymentDetails(paymentDetails)
				.build();
		
		return user;
	}
}
