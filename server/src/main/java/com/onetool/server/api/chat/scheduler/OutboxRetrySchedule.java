package com.onetool.server.api.chat.scheduler;

import com.onetool.server.api.chat.domain.ChatOutbox;
import com.onetool.server.api.chat.repository.ChatOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRetrySchedule {

    private final ChatOutboxRepository chatOutboxRepository;
    private final RedisTemplate<String, String> chatRedisTemplate;
    private final ChannelTopic channelTopic;

    @Transactional
    public void retryFailedMessages() {
        List<ChatOutbox> pendingMessages = chatOutboxRepository.findAllByStatus(ChatOutbox.OutboxStatus.PENDING);

        for (ChatOutbox message : pendingMessages) {
            try {
                chatRedisTemplate.convertAndSend(channelTopic.getTopic(), message.getPayload());
                chatRedisTemplate.opsForList().leftPush("chat:room:" + message.getRoomId(), message.getPayload());

                message.publish();
                log.info("Retry published message: {}", message.getId());
            } catch (Exception e) {
                log.error("Retry failed for outbox id: {}", message.getId(), e);
            }
        }
    }
}
