package com.elyashevich.library_service.api.controller;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.mapper.OrderMapper;
import com.elyashevich.library_service.api.validation.OnCreate;
import com.elyashevich.library_service.api.validation.OnUpdate;
import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderEntity> getAll() {
        return this.orderService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Validated(OnCreate.class) @RequestBody final OrderDto dto) {
        var order = this.orderService.create(this.orderMapper.toEntity(dto));
        return this.orderMapper.toDto(order);
    }

    @GetMapping("/{id}")
    public OrderEntity getById(@PathVariable("id") final UUID id) {
        return this.orderService.getById(id);
    }

    @PatchMapping("/{id}")
    public OrderEntity update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto
    ) {
        return this.orderService.update(id, this.orderMapper.toEntity(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.orderService.delete(id);
    }
}
