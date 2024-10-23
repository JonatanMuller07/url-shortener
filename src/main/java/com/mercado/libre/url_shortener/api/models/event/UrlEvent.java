package com.mercado.libre.url_shortener.api.models.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlEvent {

    private String action;
    private Long urlId;
    private String originalUrl;
}