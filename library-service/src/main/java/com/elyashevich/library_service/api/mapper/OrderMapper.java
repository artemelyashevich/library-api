package com.elyashevich.library_service.api.mapper;

import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.api.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends Mappable<OrderEntity, OrderDto> {
}
