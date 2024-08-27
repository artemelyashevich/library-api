package com.elyashevich.library_service.web.dto.mapper;

import com.elyashevich.library_service.domain.entity.OrderEntity;
import com.elyashevich.library_service.web.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends Mappable<OrderEntity, OrderDto> {
}
