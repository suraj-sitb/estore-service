package com.surajsitb.estore.productsMicroservice.command.interceptor;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.productsMicroservice.command.model.CreateProductCommand;
import com.surajsitb.estore.productsMicroservice.core.entity.ProductLookupEntity;
import com.surajsitb.estore.productsMicroservice.core.repository.ProductLookupRepository;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<Message<?>> {

	private final ProductLookupRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@Override
	public BiFunction<Integer, Message<?>, Message<?>> handle(List<? extends Message<?>> messages) {
		return (index, message) -> {
			if(CreateProductCommand.class.equals(message.getPayloadType())) {
				CreateProductCommand createProductCommand = (CreateProductCommand) message.getPayload();
				
				ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(
						createProductCommand.getProductId(), createProductCommand.getTitle());
				
				if (productLookupEntity != null) {
					throw new IllegalStateException(
							"Provided product :- " + createProductCommand.getTitle() + " already exists.");
				}
			}
			return message;
		};
	}

}
