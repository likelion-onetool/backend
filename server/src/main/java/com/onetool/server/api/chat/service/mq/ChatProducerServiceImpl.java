package com.onetool.server.api.chat.service.mq;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.dto.ChatMessagePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.onetool.server.api.chat.domain.ChatRabbitMqNames.EXCHANGE;
import static com.onetool.server.api.chat.domain.ChatRabbitMqNames.ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class ChatProducerServiceImpl implements ChatProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        ChatMessagePayload payload = ChatMessagePayload.from(chatMessage);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, payload);
    }
}
