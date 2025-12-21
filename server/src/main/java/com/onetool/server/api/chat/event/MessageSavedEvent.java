package com.onetool.server.api.chat.event;

import com.onetool.server.api.chat.domain.ChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageSavedEvent {
    private final ChatMessage chatMessage;
}
