package com.onetool.server.api.chat.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ChatRabbitMqNames {

    public static final String EXCHANGE = "chat.exchange";
    public static final String QUEUE = "chat.message.queue";
    public static final String ROUTING_KEY = "chat.message";
}
