package com.onetool.server.api.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("chat_room")
public class ChatRoom implements Serializable {

    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        String roomId = UUID.randomUUID().toString();
        return new ChatRoom(roomId, name);
    }
}
