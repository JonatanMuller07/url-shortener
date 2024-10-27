package com.mercado.libre.url_shortener.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlRequest {

    private String originalUrl;
    private UUID uuid;
}
