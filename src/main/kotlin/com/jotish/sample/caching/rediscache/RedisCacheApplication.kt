package com.jotish.sample.caching.rediscache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class RedisCacheApplication

fun main(args: Array<String>) {
    runApplication<RedisCacheApplication>(*args)
}
