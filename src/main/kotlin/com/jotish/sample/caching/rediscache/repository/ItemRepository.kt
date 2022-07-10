package com.jotish.sample.caching.rediscache.repository

import com.jotish.sample.caching.rediscache.data.Item
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Item, Int>
