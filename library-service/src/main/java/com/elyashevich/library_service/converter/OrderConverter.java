package com.elyashevich.library_service.converter;

import com.elyashevich.library_service.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    public OrderEntity updateConverter(OrderEntity oldOrder, OrderEntity newOrder) {
        oldOrder.setOrderIn(newOrder.getOrderIn());
        oldOrder.setExpireIn(newOrder.getExpireIn());
        return oldOrder;
    }
}
