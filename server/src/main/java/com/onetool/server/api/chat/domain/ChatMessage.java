package com.onetool.server.api.chat.domain;

import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(name = "tsid", strategy = "com.onetool.server.api.chat.domain.ChatIdGenerator")
    private Long id;

    private boolean persisted;
    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String roomId;
    private String sender;
    private String message;
}
