package com.jotish.sample.caching.rediscache.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.cache.annotation.Cacheable
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
data class ItemDto(
    var id: Int?, var description: String?
) : Serializable {
    constructor() : this(null, null) {

    }
}