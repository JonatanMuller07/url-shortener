    package com.mercado.libre.url_shortener.infraestructure.helper;

    import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
    import com.mercado.libre.url_shortener.api.models.request.UrlRequest;
    import com.mercado.libre.url_shortener.infraestructure.services.UrlService;
    import lombok.Data;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.stereotype.Component;

    import java.util.Objects;

    @Component
    @Data
    @Slf4j
    public class KafkaConsumer {

        private final UrlService urlService;
        private final JedisHelper jedisHelper;

        @KafkaListener(topics = "url-events", groupId = "url-group")
        public void listen(UrlEvent urlEvent) {
            log.info("Mensaje recibido de Kafka: {}", urlEvent);
            if(urlEvent.getAction().equals("CREATE")) {
                var url = urlService.createUrl(new UrlRequest(urlEvent.getOriginalUrl(), urlEvent.getUrlId()));
                if (Objects.nonNull(url)) {
                    jedisHelper.incrementTotalAccessCount();
                }
            }
        }
    }
