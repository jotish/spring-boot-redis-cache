package com.jotish.sample.caching.rediscache.repository

import com.jotish.sample.caching.rediscache.data.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Int> {
    fun findByIdIn(ids: List<Int>): List<Item>
}
