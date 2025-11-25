package com.onetool.server.api.chat.service.mq;

import com.onetool.server.api.chat.domain.ChatMessage;

public interface ChatProducerService {

    void sendMessage(ChatMessage messageDTO);
}
