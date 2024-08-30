package com.elyashevich.library_service.api.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "APIs for book service")
public class LibraryController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all orders",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public List<OrderEntity> getAll() {
        return this.orderService.getAll();
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Create new order",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Validated(OnCreate.class) @RequestBody final OrderDto dto) {
        var order = this.orderService.create(this.orderMapper.toEntity(dto));
        return this.orderMapper.toDto(order);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get order by id",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public OrderEntity getById(@PathVariable("id") final UUID id) {
        return this.orderService.getById(id);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update order by id",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @PatchMapping("/{id}")
    public OrderEntity update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto
    ) {
        return this.orderService.update(id, this.orderMapper.toEntity(dto));
    }


    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Delete order by id",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.orderService.delete(id);
    }
}
