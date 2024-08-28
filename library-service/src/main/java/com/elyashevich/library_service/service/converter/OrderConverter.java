package com.elyashevich.library_service.service.converter;

import com.elyashevich.library_service.entity.OrderEntity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {

    @NonNull
    public OrderEntity updateConverter(OrderEntity oldOrder, OrderEntity newOrder) {
        oldOrder.setOrderIn(newOrder.getOrderIn());
        oldOrder.setExpireIn(newOrder.getExpireIn());
        return oldOrder;
    }
}
