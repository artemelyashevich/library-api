package com.elyashevich.library_service.api.controller;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.mapper.OrderMapper;
import com.elyashevich.library_service.api.validation.OnCreate;
import com.elyashevich.library_service.api.validation.OnUpdate;
import com.elyashevich.library_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class MainController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderDto> getAll() {
        var orders = this.orderService.getAll();
        return this.orderMapper.toDto(orders);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Validated(OnCreate.class) @RequestBody final OrderDto dto) {
        var order = this.orderService.create(this.orderMapper.toEntity(dto));
        return this.orderMapper.toDto(order);
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable("id") final UUID id) {
        var order = this.orderService.getById(id);
        return this.orderMapper.toDto(order);
    }

    @PatchMapping("/{id}")
    public OrderDto update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto
    ) {
        var order = this.orderService.update(id, this.orderMapper.toEntity(dto));
        return this.orderMapper.toDto(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.orderService.delete(id);
    }
}
