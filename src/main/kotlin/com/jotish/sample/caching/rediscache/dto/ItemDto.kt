package com.jotish.sample.caching.rediscache.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemDto(
    var id: Int?, var name: String?, var description: String?
) : Serializable {
    public constructor() : this(null, null, null) {

    }
}