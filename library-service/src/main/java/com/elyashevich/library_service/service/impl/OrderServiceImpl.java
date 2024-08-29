package com.elyashevich.library_service.service.impl;

import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.exception.InvalidOrderDateException;
import com.elyashevich.library_service.exception.ResourceNotFoundException;
import com.elyashevich.library_service.repository.OrderRepository;
import com.elyashevich.library_service.service.OrderService;
import com.elyashevich.library_service.service.converter.OrderConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter converter;

    @Override
    public OrderEntity getById(final UUID id) {
        return this.orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such order with id = %s.".formatted(id)));
    }

    @Override
    public List<OrderEntity> getAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public OrderEntity create(final OrderEntity order) {
        log.debug("Attempting to create a new order: '{}'.", order);

        order.setOrderIn(LocalDateTime.now());
        if (order.getExpireIn().isAfter(order.getOrderIn())) {
            log.warn("Invalid order date.");
            throw new InvalidOrderDateException("Invalid order date.");
        }
        var newOrder = this.orderRepository.save(order);

        log.info("Order with ID '{}' has been created.", newOrder.getId());
        return newOrder;
    }

    @Override
    @Transactional
    public OrderEntity update(final UUID id, final OrderEntity order) {
        var oldOrder = this.getById(id);
        log.debug("Attempting to update the old order with ID '{}' with the new order: '{}'.", id, order);

        var result = this.orderRepository.save(this.converter.updateConverter(oldOrder, order));
        log.info("Order with ID '{}' has been updated.", id);
        return result;
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        var order = this.getById(id);
        log.debug("Attempting to delete the order with ID '{}'.", id);

        this.orderRepository.delete(order);
        log.info("Order with ID '{}' has been deleted.", id);
    }
}
