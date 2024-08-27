package com.elyashevich.library_service.service.impl;

import com.elyashevich.library_service.converter.OrderConverter;
import com.elyashevich.library_service.domain.entity.OrderEntity;
import com.elyashevich.library_service.domain.exception.NotFoundException;
import com.elyashevich.library_service.repository.OrderRepository;
import com.elyashevich.library_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderConverter converter;

    @Override
    public OrderEntity getById(final UUID id) {
        return this.orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such order with id = %s.".formatted(id)));
    }

    @Override
    public List<OrderEntity> getAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public OrderEntity create(final OrderEntity order) {
        return this.orderRepository.save(order);
    }

    @Override
    public OrderEntity update(final UUID id, final OrderEntity order) {
        var oldOrder = this.getById(id);
        return this.orderRepository.save(
                this.converter.updateConverter(oldOrder, order)
        );
    }

    @Override
    public void delete(final UUID id) {
        this.orderRepository.delete(
                this.getById(id)
        );
    }
}
