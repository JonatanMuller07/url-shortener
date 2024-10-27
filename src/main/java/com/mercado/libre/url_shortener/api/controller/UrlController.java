package com.mercado.libre.url_shortener.api.controller;

import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
import com.mercado.libre.url_shortener.api.models.request.UrlRequest;
import com.mercado.libre.url_shortener.api.models.response.UrlCreateResponse;
import com.mercado.libre.url_shortener.api.models.response.UrlResponse;
import com.mercado.libre.url_shortener.infraestructure.helper.KafkaProducer;
import com.mercado.libre.url_shortener.infraestructure.services.UrlService;
import com.mercado.libre.url_shortener.infraestructure.helper.JedisHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import lombok.Builder;


import java.util.UUID;

@RestController
@RequestMapping(path = "/url")
@AllArgsConstructor
@Builder
@Tag(name = "Url")
public class UrlController {

    private final UrlService urlService;
    private final KafkaProducer kafkaProducer;
    private final JedisHelper jedisHelper;

    @PostMapping
    public ResponseEntity<UrlCreateResponse> createUrl(@RequestBody UrlRequest urlRequest) {
        String uuid = UUID.randomUUID().toString();
        var urlResponse = UrlCreateResponse.builder()
                .originalUrl(urlRequest.getOriginalUrl())
                .id(UUID.fromString(uuid))
                .build();

        UrlEvent urlEvent = UrlEvent.builder()
                .action("CREATE")
                .urlId(UUID.fromString(uuid))
                .originalUrl(urlRequest.getOriginalUrl())
                .build();
        kafkaProducer.sendMessage("url-events", urlEvent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(urlResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlResponse> getUrl(@PathVariable UUID id) {
        UrlResponse urlResponse = urlService.getUrl(id);
        return ResponseEntity.ok(urlResponse);
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<UrlResponse> changeState(@PathVariable UUID id) {
        UrlResponse activeChange = urlService.setActive(id);
        return ResponseEntity.ok(activeChange);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UrlResponse> updateUrl(@PathVariable UUID id, @RequestParam String newDestination) {
        var updatedUrl =  urlService.updateUrl(id, newDestination);
        return ResponseEntity.ok(updatedUrl);
    }

    @GetMapping("/stats/{id}")
    public String getUrlStats(@PathVariable String id) {
        long accessCount = jedisHelper.getAccessCountById(UUID.fromString(id));
        return "Accesos para el ID: " + id + "es de: " + accessCount;
    }

    @GetMapping("/stats/total")
    public String getTotalStats() {
        long totalAccesses = jedisHelper.getTotalAccesses();
        return "Accesos totales: " + totalAccesses;
    }
}
