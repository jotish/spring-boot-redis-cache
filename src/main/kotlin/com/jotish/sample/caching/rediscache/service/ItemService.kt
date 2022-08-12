package com.jotish.sample.caching.rediscache.service

import com.jotish.sample.caching.rediscache.constants.CacheConstants
import com.jotish.sample.caching.rediscache.data.Item
import com.jotish.sample.caching.rediscache.dto.ItemDto
import com.jotish.sample.caching.rediscache.dto.ItemRequestDto
import com.jotish.sample.caching.rediscache.mapper.ItemMapper
import com.jotish.sample.caching.rediscache.repository.ItemRepository
import com.jotish.sample.caching.rediscache.util.RedisUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException
import javax.validation.Valid


@Service
class ItemService {

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemMapper: ItemMapper

    @Autowired
    private lateinit var redisUtil: RedisUtil<String, ItemDto>

    companion object {
        private val log = LoggerFactory.getLogger(ItemService::class.java)
    }


    @Cacheable(value = [CacheConstants.CACHE_KEY_ITEM], cacheManager = CacheConstants.CACHE_MANAGER_REDIS)
    fun getItemForId(id: Int): ItemDto {
        val item: Item = itemRepository.findById(id).orElseThrow { EntityNotFoundException() }
        return itemMapper.convertToDto(item)
    }


    @Transactional
    fun save(itemRequestDto: @Valid ItemRequestDto): ItemDto {
        val item = itemMapper.convertToModel(itemRequestDto)
        itemRepository.save(item)
        return itemMapper.convertToDto(item)
    }

    @CacheEvict(value = [CacheConstants.CACHE_KEY_ITEM], key = "#id", cacheManager = CacheConstants.CACHE_MANAGER_REDIS)
    @Transactional
    fun update(id: Int, itemRequestDto: @Valid ItemRequestDto): ItemDto {
        val item: Item = itemRepository.findById(id).orElseThrow { EntityNotFoundException() }
        itemMapper.update(item, itemRequestDto)
        itemRepository.save(item)
        return itemMapper.convertToDto(item)
    }

    fun getItems(ids: List<Int>): List<ItemDto> {
        val result = mutableListOf<ItemDto>()
        val cacheKeys: List<String> = ids.map { CacheConstants.CACHE_KEY_ITEM + "::" + it }
        // Assumption is that the order of keys sent is the same order in which
        // mget responds so keys which are not cached will have the values as null
        val cachedItems: MutableList<ItemDto?> = redisUtil.multiGet(cacheKeys)

        val nonCachedIds = mutableListOf<Int>()
        cachedItems.forEachIndexed { index, item ->
            if (item == null) {
                nonCachedIds.add(ids[index])
            } else {
                log.info("Found item in cache ${item.id}")
                result.add(item)
            }
        }
        val items = itemRepository.findByIdIn(nonCachedIds)
        val nonCachedItems = itemMapper.convertToDtos(items)
        result.addAll(nonCachedItems)
        return result
    }
}