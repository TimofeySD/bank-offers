package com.example.productservice.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig implements CachingConfigurer {

    public static final String PRODUCTS_CACHE = "products";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .prefixCacheNameWith("product-service::")
            .entryTtl(Duration.ofMinutes(5));
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder builder = new StringBuilder(method.getName());
            for (Object param : params) {
                builder.append(":");
                builder.append(param == null ? "null" : param.toString());
            }
            return builder.toString();
        };
    }

    @Bean
    public CacheManagerCustomizer<RedisCacheManager> redisCacheManagerCustomizer() {
        return cacheManager -> {
            if (cacheManager.getCache(PRODUCTS_CACHE) == null) {
                cacheManager.createCache(PRODUCTS_CACHE, RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                    .prefixCacheNameWith("product-service::")
                    .entryTtl(Duration.ofMinutes(5)));
            }
        };
    }
}
