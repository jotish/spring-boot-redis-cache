package com.jotish.sample.caching.rediscache.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
class CacheConfig {


    @Bean
    fun cacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues().entryTtl(Duration.ofMinutes(60))
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
    }


    @Bean
    @Primary
    fun cacheManager(): CacheManager? {
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(
            Caffeine.newBuilder().initialCapacity(2000).expireAfterWrite(60 * 5, TimeUnit.MINUTES).maximumSize(50000)
        )
        return cacheManager
    }

    @Bean("redisCacheManager")
    fun redisCacheManager(lettuceConnectionFactory: LettuceConnectionFactory): CacheManager? {
        return RedisCacheManager(
            RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory), cacheConfiguration()
        )
    }

    // Use Default Serialisation
//    @Bean
//    fun cacheConfiguration(): RedisCacheConfiguration {
//        return RedisCacheConfiguration.defaultCacheConfig()
//            .disableCachingNullValues()
//            .entryTtl(Duration.ofMinutes(60))
//    }


}