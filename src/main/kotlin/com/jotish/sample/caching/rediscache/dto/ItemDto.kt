package com.jotish.sample.caching.rediscache.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemDto(
    var id: Int?, var description: String?
) : Serializable {
    constructor() : this(null, null) {

    }
}