package com.mercado.libre.url_shortener.api.models.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlEvent {

    private String action;
    private UUID urlId;
    private String originalUrl;
}