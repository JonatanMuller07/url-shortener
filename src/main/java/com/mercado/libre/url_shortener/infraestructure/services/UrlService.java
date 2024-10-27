package com.mercado.libre.url_shortener.infraestructure.services;

import com.mercado.libre.url_shortener.api.models.request.UrlRequest;
import com.mercado.libre.url_shortener.api.models.response.UrlResponse;
import com.mercado.libre.url_shortener.domain.entities.UrlEntity;
import com.mercado.libre.url_shortener.domain.repository.UrlRepository;
import com.mercado.libre.url_shortener.util.CacheConstants;
import com.mercado.libre.url_shortener.infraestructure.helper.JedisHelper;
import com.mercado.libre.url_shortener.util.UrlUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final JedisHelper jedisHelper;

    public UrlResponse createUrl(String originalUrl, UUID uuid) {
        var urlToPersist = new UrlEntity().builder()
                .originalUrl(originalUrl)
                .shortUrl(UrlUtil.shortenUrl())
                .id(uuid)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        var urlPersist = urlRepository.save(urlToPersist);
        return entityToResponse(urlPersist);
    }

    @Cacheable(value = CacheConstants.URL_CACHE_NAME, key = "#id")
    public UrlResponse getUrl(UUID id) {
        var urlFromDb = urlRepository.findById(id).orElseThrow();
        jedisHelper.incrementAccessCount(urlFromDb.getId());
        return entityToResponse(urlFromDb);
    }

    @CacheEvict(value = CacheConstants.URL_CACHE_NAME, key = "#id")
    public UrlResponse setActive(UUID id) {
        UrlEntity urlToUpdate = urlRepository.findById(id).orElseThrow();
        urlToUpdate.setActive(!urlToUpdate.getActive());
        var urlUpdated = urlRepository.save(urlToUpdate);
        jedisHelper.incrementAccessCount(urlUpdated.getId());
        return entityToResponse(urlUpdated);
    }

    @CacheEvict(value = CacheConstants.URL_CACHE_NAME, key = "#id")
    public UrlResponse updateUrl(UUID id, String newDestination) {
        UrlEntity urlToUpdate = urlRepository.findById(id).orElseThrow();
        urlToUpdate.setOriginalUrl(newDestination);
        var urlUpdated = urlRepository.save(urlToUpdate);
        jedisHelper.incrementAccessCount(urlUpdated.getId());
        return entityToResponse(urlUpdated);
    }

    private UrlResponse entityToResponse(UrlEntity urlEntity) {
        UrlResponse urlResponse = new UrlResponse();
        BeanUtils.copyProperties(urlEntity, urlResponse);
        return urlResponse;
    }
}
