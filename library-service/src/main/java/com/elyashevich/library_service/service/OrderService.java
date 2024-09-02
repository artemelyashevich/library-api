package com.elyashevich.library_service.service;

import com.elyashevich.library_service.entity.OrderEntity;

import java.util.List;
import java.util.UUID;

/**
 * Interface for managing orders.
 */
public interface OrderService {

    /**
     * Retrieve an order by its unique identifier.
     *
     * @param id the unique identifier of the order
     * @return the order with the specified ID
     */
    OrderEntity getById(final UUID id);

    /**
     * Retrieve all orders.
     *
     * @return a list of all orders
     */
    List<OrderEntity> getAll();

    /**
     * Retrieve all active orders.
     *
     * @return a list of active all orders
     */
    List<OrderEntity> getAllActive();

    /**
     * Create a new order.
     *
     * @param order the order to be created
     * @return the created order
     */
    OrderEntity create(final OrderEntity order);

    /**
     * Update an existing order with the specified ID.
     *
     * @param id    the unique identifier of the order to be updated
     * @param order the updated order information
     * @return the updated order
     */
    OrderEntity update(final UUID id, final OrderEntity order);

    /**
     * Delete an order by its unique identifier.
     *
     * @param id the unique identifier of the order to be deleted
     */
    void delete(final UUID id);
}
