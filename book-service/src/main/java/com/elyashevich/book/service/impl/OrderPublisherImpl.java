package com.elyashevich.book.service.impl;

import com.elyashevich.book.api.dto.OrderDto;
import com.elyashevich.book.service.OrderPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPublisherImpl implements OrderPublisher {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Value("${application.kafka.topic:order}")
    private String topicName;

    @Override
    public void sendMessage(final OrderDto orderDto) {
        log.debug("Try to send an order message: {}", orderDto);

        var message = MessageBuilder
                .withPayload(orderDto.toString())
                .setHeader(TOPIC, this.topicName)
                .build();
        this.kafkaTemplate.send(message);

        log.info("Order '{}' message has been successfully sent.", message.getHeaders().getId());
    }
}
