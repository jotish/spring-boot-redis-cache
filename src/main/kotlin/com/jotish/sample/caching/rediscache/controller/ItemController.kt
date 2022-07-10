package com.jotish.sample.caching.rediscache.controller

import com.jotish.sample.caching.rediscache.dto.ItemDto
import com.jotish.sample.caching.rediscache.dto.ItemRequestDto
import com.jotish.sample.caching.rediscache.service.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ItemController {

    @Autowired
    private lateinit var itemService: ItemService

    @GetMapping("/item/{id}")
    fun getItemById(@PathVariable id: Int): ItemDto {
        return itemService.getItemForId(id)
    }


    @PostMapping("/items")
    fun post(@RequestBody itemRequest: ItemRequestDto): ItemDto {
        return itemService.save(itemRequest)
    }

    @PatchMapping("/item/{id}")
    fun patch(@PathVariable id: Int, @RequestBody itemRequest: ItemRequestDto): ItemDto {
        return itemService.update(id, itemRequest)
    }
}