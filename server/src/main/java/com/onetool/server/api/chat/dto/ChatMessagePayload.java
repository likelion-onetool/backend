package com.onetool.server.api.chat.dto;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import lombok.Builder;

import java.time.ZoneId;

@Builder
public record ChatMessagePayload(
        Long id,
        String roomId,
        String sender,
        String message,
        MessageType type,
        Long createdAt
) {

    public static ChatMessagePayload from(ChatMessage chatMessage) {
        Long createdAtMillis = null;
        if (chatMessage.getCreatedAt() != null) {
            createdAtMillis = chatMessage.getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
        }

        return ChatMessagePayload.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .type(chatMessage.getType())
                .build();
    }

    public ChatMessage toChatMessage() {
        return ChatMessage. builder()
                .id(this.id)
                .roomId(this.roomId)
                .sender(this.sender)
                .message(this.message)
                .type(this.type)
                .build();
    }
}
