package com.example.synchrony.Configurations;

import org.apache.camel.builder.RouteBuilder;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;



/**Apache Camel directional routing and sending it to the kafka brokers **/
@Component
public class Configuration extends RouteBuilder {
    @Value("${spring.kafka.bootstrap-servers}") // Inject the Kafka broker address from application.properties
    private String kafkaBrokerAddress;
    @Override
    public void configure() throws Exception {
        // Route from direct:publishToMessagingPlatform to a Kafka topic with hardcoded broker address
        from("direct:publishToMessagingPlatform")
                .setHeader("KafkaTopic", constant("user-topic")) // Set the Kafka topic
                .to("kafka:" + kafkaBrokerAddress + "?topic=${exchangeProperty.KafkaTopic}");
    }
    }


