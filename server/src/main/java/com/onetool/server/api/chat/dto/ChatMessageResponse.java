package com.onetool.server.api.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(
        @JsonSerialize(using = ToStringSerializer.class)
        Long id,
        MessageType type,
        String sender,
        String message,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse from(ChatMessage message) {
        return ChatMessageResponse.builder()
                .type(message.getType())
                .sender(message.getSender())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
