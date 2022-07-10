package com.jotish.sample.caching.rediscache.dto

import javax.validation.constraints.NotBlank

data class ItemRequestDto(

    @NotBlank(message = "Name may not be blank") var name: String,

    @NotBlank(message = "Description may not be blank") var description: String
)