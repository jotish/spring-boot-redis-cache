package com.jotish.sample.caching.rediscache.service

import com.jotish.sample.caching.rediscache.data.Item
import com.jotish.sample.caching.rediscache.dto.ItemDto
import com.jotish.sample.caching.rediscache.dto.ItemRequestDto
import com.jotish.sample.caching.rediscache.mapper.ItemMapper
import com.jotish.sample.caching.rediscache.repository.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid


@Service
class ItemService {

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemMapper: ItemMapper

    @Cacheable(value = ["itemCache"])
    fun getItemForId(id: Int): ItemDto {
        val item: Item = itemRepository.findById(id).orElseThrow { RuntimeException() }
        return itemMapper.convertToDto(item)
    }


    @Transactional
    fun save(itemRequestDto: @Valid ItemRequestDto): ItemDto {
        val item = itemMapper.convertToModel(itemRequestDto)
        itemRepository.save(item)
        return itemMapper.convertToDto(item)
    }

    @CacheEvict(value = ["itemCache"], key = "#id")
    fun update(id: Int, itemRequestDto: @Valid ItemRequestDto): ItemDto {
        val item: Item = itemRepository.findById(id).orElseThrow { RuntimeException() }
        itemMapper.update(item, itemRequestDto)
        itemRepository.save(item)
        return itemMapper.convertToDto(item)
    }
}