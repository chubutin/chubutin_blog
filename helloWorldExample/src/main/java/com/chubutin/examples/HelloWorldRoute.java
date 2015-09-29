package com.chubutin.examples;

import org.apache.camel.builder.RouteBuilder;

public class HelloWorldRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:helloWorld").routeId("helloRoute")
				.log("body message ${body}")
				.to("file:src/test/resources/data/output/out.txt");

	}

}
