package com.elyashevich.library_service.api.controller;

import com.elyashevich.library_service.api.dto.ExceptionBody;
import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.validation.OnCreate;
import com.elyashevich.library_service.api.validation.OnUpdate;
import com.elyashevich.library_service.entity.OrderEntity;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/library")
@Tag(name = "Book Controller", description = "APIs for book service")
public interface LibraryController {

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
    List<OrderDto> getAll();

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
    OrderDto create(@Validated(OnCreate.class) @RequestBody final OrderDto dto);

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
    OrderEntity getById(@PathVariable("id") final UUID id);

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
    OrderEntity update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final OrderDto dto
    );

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
    void delete(@PathVariable("id") final UUID id);

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all active orders",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping("/active")
    List<OrderDto> getAllActive();
}
