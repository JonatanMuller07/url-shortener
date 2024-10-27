package com.mercado.libre.url_shortener.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlCreateResponse {

    private UUID id;
    private String originalUrl;
}
