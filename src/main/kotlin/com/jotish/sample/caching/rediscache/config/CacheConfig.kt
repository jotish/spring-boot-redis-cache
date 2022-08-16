package com.jotish.sample.caching.rediscache.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.benmanes.caffeine.cache.Caffeine
import com.jotish.sample.caching.rediscache.constants.CacheConstants
import com.jotish.sample.caching.rediscache.dto.ItemDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
class CacheConfig {


    @Bean
    fun cacheConfiguration(@Qualifier(CacheConstants.CACHE_JSON_OBJECT_MAPPER) om: ObjectMapper): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(60))
            .serializeKeysWith(SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(om)))
    }

    @Bean()
    @Primary
    fun objectMapper(): ObjectMapper {
        val om = ObjectMapper()
        om.registerKotlinModule()
        return om
    }

    @Bean(CacheConstants.CACHE_JSON_OBJECT_MAPPER)
    fun redisJsonObjectMapper(): ObjectMapper {
        val om = ObjectMapper()
        om.registerKotlinModule()
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.WRAPPER_ARRAY
        )

        return om
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

    @Bean(CacheConstants.CACHE_MANAGER_REDIS)
    fun redisCacheManager(
        lettuceConnectionFactory: LettuceConnectionFactory,
        redisCacheConfiguration: RedisCacheConfiguration
    ): CacheManager? {
        return RedisCacheManager(
            RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory),
            redisCacheConfiguration
        )
    }


    @Bean(CacheConstants.REDIS_TEMPLATE_FOR_ITEM)
    fun redisTemplate(
        lettuceConnectionFactory: LettuceConnectionFactory,
        @Qualifier(CacheConstants.CACHE_JSON_OBJECT_MAPPER) objectMapper: ObjectMapper
    ): RedisTemplate<String, ItemDto> {
        val template = RedisTemplate<String, ItemDto>()
        template.setConnectionFactory(lettuceConnectionFactory)
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(ItemDto::class.java)
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper)
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = jackson2JsonRedisSerializer
        template.hashValueSerializer = jackson2JsonRedisSerializer
        template.valueSerializer = jackson2JsonRedisSerializer
        template.afterPropertiesSet()
        return template
    }

    // Use Default Serialisation
//    @Bean
//    fun cacheConfiguration(): RedisCacheConfiguration {
//        return RedisCacheConfiguration.defaultCacheConfig()
//            .disableCachingNullValues()
//            .entryTtl(Duration.ofMinutes(60))
//    }


}