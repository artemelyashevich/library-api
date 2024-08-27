package com.elyashevich.library_service.service;

import com.elyashevich.library_service.domain.entity.OrderEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderEntity getById(UUID id);

    List<OrderEntity> getAll();

    OrderEntity create(OrderEntity order);

    OrderEntity update(UUID id, OrderEntity order);

    void delete(UUID id);
}
