package com.onetool.server.api.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RedisHash("chat_room")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public static ChatRoom create(String name) {
        String roomId = UUID.randomUUID().toString();
        return new ChatRoom(roomId, name);
    }
}
