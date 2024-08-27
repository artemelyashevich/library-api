package com.elyashevich.library_service.web.controller;

import com.elyashevich.library_service.service.OrderService;
import com.elyashevich.library_service.web.dto.OrderDto;
import com.elyashevich.library_service.web.dto.mapper.OrderMapper;
import com.elyashevich.library_service.web.validation.OnCreate;
import com.elyashevich.library_service.web.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class MainController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity
                .ok()
                .body(
                        this.orderMapper.toDto(
                                this.orderService.getAll()
                        )
                );
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(
            @Validated(OnCreate.class) @RequestBody final OrderDto dto,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        var order = this.orderService.create(
                this.orderMapper.toEntity(dto)
        );
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .replacePath("/api/v1/orders/{id}")
                                .build(Map.of("id", order.getId()))
                )
                .body(
                        this.orderMapper.toDto(order)
                );
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable("id") final UUID id) {
        return ResponseEntity
                .ok()
                .body(
                        this.orderMapper.toDto(
                                this.orderService.getById(id)
                        )
                );
    }

    @PatchMapping("{id}")
    public ResponseEntity<OrderDto> update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        var order = this.orderService.update(
                id,
                this.orderMapper.toEntity(dto)
        );
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .replacePath("/api/v1/orders/{id}")
                                .build(Map.of("id", order.getId()))
                )
                .body(
                        this.orderMapper.toDto(order)
                );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID id) {
        this.orderService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
