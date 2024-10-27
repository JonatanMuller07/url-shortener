    package com.mercado.libre.url_shortener.infraestructure.helper;

    import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
    import com.mercado.libre.url_shortener.api.models.request.UrlRequest;
    import com.mercado.libre.url_shortener.infraestructure.services.UrlService;
    import lombok.Data;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.retry.annotation.Backoff;
    import org.springframework.retry.annotation.Retryable;
    import org.springframework.stereotype.Component;

    import java.util.Objects;

    @Component
    @Data
    @Slf4j
    public class KafkaConsumer {

        private final UrlService urlService;
        private final JedisHelper jedisHelper;

        @Retryable(
                value = {Exception.class},
                maxAttempts = 10,
                backoff = @Backoff(delay = 2000, multiplier = 2)
        )
        @KafkaListener(topics = "url-events", groupId = "url-group")
        public void listen(UrlEvent urlEvent) {
            log.info("Mensaje recibido de Kafka: {}", urlEvent);
            try {
                if(urlEvent.getAction().equals("CREATE")) {
                    var url = urlService.createUrl(urlEvent.getOriginalUrl(), urlEvent.getUrlId());
                    if (Objects.nonNull(url)) {
                        jedisHelper.incrementTotalAccessCount();
                    }
                }
            } catch (Exception e) {
                log.error("Error procesando el mensaje de Kafka: {}", e.getMessage());
                throw e;
            }
        }
    }
