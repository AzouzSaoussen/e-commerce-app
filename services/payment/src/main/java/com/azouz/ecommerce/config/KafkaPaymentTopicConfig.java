package com.azouz.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaPaymentTopicConfig {
    // The payment topic that will send all the payment notification
    @Bean
    public NewTopic paymentTopic(){
        return TopicBuilder
                .name("payment-topic")
                .build();
    }
}
