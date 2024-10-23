package com.mercado.libre.url_shortener.infraestructure.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String topic, UrlEvent urlEvent) {
        try {
            String message = objectMapper.writeValueAsString(urlEvent);
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
