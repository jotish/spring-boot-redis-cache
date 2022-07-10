package com.jotish.sample.caching.rediscache.data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Item(

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id var id: Int?,

    var name: String?,

    var description: String?
) {
    public constructor() : this(null, null, null) {

    }
}