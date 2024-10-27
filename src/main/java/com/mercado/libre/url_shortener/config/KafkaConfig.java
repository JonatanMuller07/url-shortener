package com.mercado.libre.url_shortener.config;

import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-v5590.asia-northeast2.gcp.confluent.cloud:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username='3DYTNNBNGYJOO6VC' password='Q/lOr1HAIKZRk54+h0iNdyunAdwlVA2qFkuumenZwd6Bcp9jQtlOj0T5UVt64X0l';");
        config.put("sasl.mechanism", "PLAIN");
        config.put("security.protocol", "SASL_SSL");
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, UrlEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-v5590.asia-northeast2.gcp.confluent.cloud:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "url-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UrlEvent.class.getName());
        config.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class.getName());
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='3DYTNNBNGYJOO6VC' password='Q/lOr1HAIKZRk54+h0iNdyunAdwlVA2qFkuumenZwd6Bcp9jQtlOj0T5UVt64X0l';");
        config.put("sasl.mechanism", "PLAIN");
        config.put("security.protocol", "SASL_SSL");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UrlEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UrlEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
