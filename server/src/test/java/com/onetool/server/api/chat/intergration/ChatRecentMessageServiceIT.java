package com.onetool.server.api.chat.intergration;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.service.ChatRecentMessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatRecentMessageServiceIT {

    @Autowired
    private ChatRecentMessageService chatRecentMessageService;

    @Autowired
    private RedisTemplate<String, String> chatRedisTemplate;

    @AfterEach
    void tearDown() {
        chatRedisTemplate.delete("chat:room:main:messages");
    }

    @Test
    void push와_get으로_최근메시지를_정상적으로_주고받는다() {
        // given
        ChatMessage msg1 = ChatMessage.builder()
                .type(MessageType.TALK)
                .roomId("main")
                .sender("user1")
                .message("hello")
                .build();

        ChatMessage msg2 = ChatMessage.builder()
                .type(MessageType.TALK)
                .roomId("main")
                .sender("user2")
                .message("world")
                .build();

        // when
        chatRecentMessageService.pushRecentMessage(msg1);
        chatRecentMessageService.pushRecentMessage(msg2);

        // then
        List<ChatMessage> recent = chatRecentMessageService.getRecentMessages("main");
        Assertions.assertEquals(2, recent.size());
        Assertions.assertEquals("world", recent.get(0).getMessage());
        Assertions.assertEquals("hello", recent.get(1).getMessage());
    }
}
