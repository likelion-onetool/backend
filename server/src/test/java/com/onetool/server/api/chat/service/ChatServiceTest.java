package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.repository.ChatRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Ignore
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private RedisTemplate<String, String> chatRedisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @Mock
    private ChannelTopic channelTopic;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Redis 저장 실패 시 RuntimeException을 던져 트랜잭션이 롤백되어야 한다")
    void saveMessage_WhenRedisFails_ShouldThrowRuntimeException() throws Exception {
        // given
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId("test-room")
                .sender("test-sender")
                .message("test-message")
                .build();
        String messageJson = "{\"roomId\":\"test-room\",\"sender\":\"test-sender\",\"message\":\"test-message\"}";

        when(chatRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(objectMapper.writeValueAsString(any(ChatMessage.class))).thenReturn(messageJson);

        when(chatRedisTemplate.opsForList()).thenReturn(listOperations);
        when(channelTopic.getTopic()).thenReturn("chat");

        doThrow(new RuntimeException("Redis connection failed")).when(chatRedisTemplate).convertAndSend(anyString(), anyString());

        // when & then
        assertThrows(RuntimeException.class, () -> chatService.saveMessage(chatMessage));

        verify(chatRepository, times(1)).save(any(ChatMessage.class));
        verify(chatRedisTemplate, times(1)).convertAndSend(anyString(), anyString());
    }

    @Test
    @DisplayName("JSON 직렬화 실패 시 RuntimeException을 던져 트랜잭션이 롤백되어야 한다")
    void saveMessage_WhenSerializationFails_ShouldThrowRuntimeException() throws Exception {
        // given
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId("test-room")
                .sender("test-sender")
                .message("test-message")
                .build();

        when(chatRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(objectMapper.writeValueAsString(any(ChatMessage.class))).thenThrow(JsonProcessingException.class);

        // when & then
        assertThrows(RuntimeException.class, () -> chatService.saveMessage(chatMessage));

        verify(chatRepository, times(1)).save(any(ChatMessage.class));
        verify(chatRedisTemplate, never()).convertAndSend(anyString(), anyString());
    }
}
