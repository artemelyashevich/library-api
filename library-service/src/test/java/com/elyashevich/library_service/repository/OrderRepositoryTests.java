package com.elyashevich.library_service.repository;

import com.elyashevich.library_service.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @ParameterizedTest
    @MethodSource("provideOrder")
    void orderRepository_save(final OrderEntity order, final UUID orderId) {
        // Act
        var savedOrder = this.orderRepository.save(order);

        // Assert
        assertAll(
                () -> assertThat(savedOrder).isNotNull(),
                () -> assertThat(savedOrder.getId()).isNotNull()
        );
    }

    @Test
    void orderRepository_findAll() {
        // Act
        var orders = this.orderRepository.findAll();

        // Assert
        assertAll(
                () -> assertThat(orders).isNotNull(),
                () -> assertThat(orders.size()).isEqualTo(2)
        );
    }

    private static Stream<Arguments> provideOrder() {
        var orderId = UUID.randomUUID();
        var order = new OrderEntity();
        order.setId(orderId);
        return Stream.of(
            Arguments.of(order, orderId)
        );
    }
}
