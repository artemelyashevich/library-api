package com.elyashevich.library_service.api.controller.impl;

import com.elyashevich.library_service.api.controller.LibraryController;
import com.elyashevich.library_service.api.dto.ExceptionBody;
import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.mapper.OrderMapper;
import com.elyashevich.library_service.api.validation.OnCreate;
import com.elyashevich.library_service.api.validation.OnUpdate;
import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.service.OrderService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LibraryControllerImpl implements LibraryController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getAll() {
        var orders = this.orderService.getAll();
        return this.orderMapper.toDto(orders);
    }

    @Override
    public OrderDto create(@Validated(OnCreate.class) @RequestBody final OrderDto dto) {
        var order = this.orderService.create(this.orderMapper.toEntity(dto));
        return this.orderMapper.toDto(order);
    }

    @Override
    public OrderEntity getById(@PathVariable("id") final UUID id) {
        return this.orderService.getById(id);
    }

    @Override
    public OrderEntity update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto
    ) {
        return this.orderService.update(id, this.orderMapper.toEntity(dto));
    }


    @Override
    public void delete(@PathVariable("id") final UUID id) {
        this.orderService.delete(id);
    }

    @Override
    public List<OrderDto> getAllActive() {
        var orders = this.orderService.getAllActive();
        return this.orderMapper.toDto(orders);
    }
}
