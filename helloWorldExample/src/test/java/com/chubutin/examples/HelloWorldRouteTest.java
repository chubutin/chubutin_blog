package com.chubutin.examples;

import java.io.File;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class HelloWorldRouteTest extends CamelTestSupport {
	
	String fileURL = "src/test/resources/data/outbox";
	
	@Test
	public void testSendMessage() throws InterruptedException{
		
		//set up the mock endpoint for check certain point in the route
		MockEndpoint mockEndpoint = getMockEndpoint("mock:mockEndpoint");
		//configure minimun amount of messages in the endpoint
		mockEndpoint.expectedMinimumMessageCount(1);
		
		//send to the endpoint direct:helloWorld the message 'hola en espaniol'
		template.sendBody("direct:helloWorld", "hola en espaniol " + System.currentTimeMillis());
		
		File outputFile = new File(fileURL);
		
		//check if the output file exist
		assertTrue("The output file doesn't exist", outputFile.exists());
		
		//assert to the mockEndpoint
		mockEndpoint.assertIsSatisfied(1000l);
		
	}

	@Before
	public void setUp() throws Exception {
		deleteDirectory("src/test/resources/data/output");
		super.setUp();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:helloWorld").routeId("helloRoute")
						.log(LoggingLevel.INFO, "body message ${body}")
						.to("file://" + fileURL+"?fileName=testfile.txt")
						.to("mock:mockEndpoint");

			}
		};
	}
	
}
