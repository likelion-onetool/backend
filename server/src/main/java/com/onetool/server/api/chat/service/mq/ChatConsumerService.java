package com.onetool.server.api.chat.service.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onetool.server.api.chat.dto.ChatMessagePayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.onetool.server.api.chat.domain.ChatRabbitMqNames.QUEUE;

public interface ChatConsumerService {

    @RabbitListener(queues = QUEUE)
    void consume(ChatMessagePayload payload) throws JsonProcessingException;
}
