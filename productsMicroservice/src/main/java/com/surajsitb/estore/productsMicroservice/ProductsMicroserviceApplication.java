package com.surajsitb.estore.productsMicroservice;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.surajsitb.estore.productsMicroservice.command.interceptor.CreateProductCommandInterceptor;
import com.surajsitb.estore.productsMicroservice.core.errorHandling.ProductsServiceEventsErrorHandler;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsMicroserviceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext applicationContext,
			CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
	}
	
	@Autowired
	public void handleProductsServiceEventsErrorHandler(EventProcessingConfigurer configurer) {
		configurer.registerListenerInvocationErrorHandler("product-group", 
				conf -> new ProductsServiceEventsErrorHandler());
	}
	
//	@Autowired
//	public void handleProductsServiceEventsErrorHandler(EventProcessingConfigurer configurer) {
//		configurer.registerListenerInvocationErrorHandler("product-group", 
//				conf -> PropagatingErrorHandler.instance());
//	}
}
