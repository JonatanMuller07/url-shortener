package com.mercado.libre.url_shortener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:configs/redis.properties")
public class PropertyConfig {
}
