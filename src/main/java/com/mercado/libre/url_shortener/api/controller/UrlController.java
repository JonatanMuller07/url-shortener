package com.mercado.libre.url_shortener.api.controller;

import com.mercado.libre.url_shortener.api.models.event.UrlEvent;
import com.mercado.libre.url_shortener.api.models.request.UrlRequest;
import com.mercado.libre.url_shortener.api.models.response.UrlResponse;
import com.mercado.libre.url_shortener.infraestructure.helper.KafkaProducer;
import com.mercado.libre.url_shortener.infraestructure.services.UrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/url")
@AllArgsConstructor
@Builder
@Tag(name = "Url")
public class UrlController {

    private final UrlService urlService;
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<UrlResponse> createUrl(@RequestBody UrlRequest urlRequest) {
        UrlEvent urlEvent = UrlEvent.builder()
                .action("CREATE")
                .urlId(0L)
                .originalUrl(urlRequest.getOriginalUrl())
                .build();
        kafkaProducer.sendMessage("url-events", urlEvent);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlResponse> getUrl(@PathVariable Long id) {
        UrlResponse urlResponse = urlService.getUrl(id);
        return ResponseEntity.ok(urlResponse);
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<UrlResponse> changeState(@PathVariable Long id) {
        UrlResponse activeChange = urlService.setActive(id);
//        String state = activeChange.getActive() ? "Fue activada" : "Fue desactivada";
//        kafkaProducer.sendMessage("url-events", state + "la URL con ID: " + id);
        return ResponseEntity.ok(activeChange);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UrlResponse> updateUrl(@PathVariable Long id, @RequestParam String newDestination) {
        var updatedUrl =  urlService.updateUrl(id, newDestination);
        return ResponseEntity.ok(updatedUrl);
    }
}
