package com.jotish.sample.caching.rediscache.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.cache.annotation.Cacheable
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
// @Cacheable is must if you want to cache type information while using @Cacheable
// Refer this for more details
// https://stackoverflow.com/questions/52265326/how-can-i-easily-cache-kotlin-objects-in-redis-using-json-via-jackson
data class ItemDto(
    var id: Int?, var description: String?
) : Serializable {
    constructor() : this(null, null) {

    }
}