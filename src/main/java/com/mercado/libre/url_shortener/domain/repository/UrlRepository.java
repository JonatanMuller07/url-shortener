package com.mercado.libre.url_shortener.domain.repository;

import com.mercado.libre.url_shortener.domain.entities.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UrlRepository extends JpaRepository<UrlEntity, UUID> {
}