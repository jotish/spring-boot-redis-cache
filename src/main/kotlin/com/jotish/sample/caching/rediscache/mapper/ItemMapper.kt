package com.jotish.sample.caching.rediscache.mapper

import com.jotish.sample.caching.rediscache.data.Item
import com.jotish.sample.caching.rediscache.dto.ItemDto
import com.jotish.sample.caching.rediscache.dto.ItemRequestDto
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface ItemMapper {

    fun convertToModel(itemDto: ItemRequestDto): Item

    fun convertToDto(item: Item): ItemDto

    fun update(@MappingTarget item: Item, itemDto: ItemRequestDto)

}