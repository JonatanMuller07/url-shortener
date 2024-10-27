package com.mercado.libre.url_shortener.api.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "La URL no puede estar vacía")
    @Pattern(regexp = "^(http|https)://.*$", message = "La URL debe ser válida y comenzar con http o https")
    private String originalUrl;
}
