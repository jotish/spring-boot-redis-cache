package com.jotish.sample.caching.rediscache.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration


@Configuration
class CacheConfig {

    @Bean
    fun cacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(60))
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
    }


//    Use Default Serialisation
//    @Bean
//    fun cacheConfiguration(): RedisCacheConfiguration {
//        return RedisCacheConfiguration.defaultCacheConfig()
//            .disableCachingNullValues()
//            .entryTtl(Duration.ofMinutes(60))
//    }
}