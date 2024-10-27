package com.mercado.libre.url_shortener.infraestructure.helper;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Component
@AllArgsConstructor
public class JedisHelper {

    @Autowired
    private Jedis jedis;

    /**
     * Método para incrementar el acceso total
     */
    public void incrementTotalAccessCount() {
        jedis.incr("total_url_access");
    }

    /**
     * Método para incrementar los accesos por ID
     */
    public void incrementAccessCount(UUID id) {
        jedis.incr("url_access:" + id);
    }

    /**
     * Método para obtener el conteo de accesos por ID
     * @param id
     * @return
     */
    public long getAccessCountById(UUID id) {
        String accessCount = jedis.get("url_access:" + id);
        return accessCount != null ? Long.parseLong(accessCount) : 0;
    }

    /**
     * Método para obtener estadísticas generales
     * @return
     */
    public long  getTotalAccesses() {
        String totalAccessCount = jedis.get("total_url_access");
        return totalAccessCount != null ? Long.parseLong(totalAccessCount) : 0;
    }
}
