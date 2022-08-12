package com.jotish.sample.caching.rediscache.util


import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisUtil<String, V : Any>(private val redisTemplate: RedisTemplate<String, V>) {

    companion object {
        private val log = LoggerFactory.getLogger(RedisUtil::class.java)
    }

    fun multiGet(keys: Collection<String>): MutableList<V?> {
        log.debug("multiGet $keys")
        return redisTemplate.opsForValue().multiGet(keys) ?: mutableListOf()
    }
}
