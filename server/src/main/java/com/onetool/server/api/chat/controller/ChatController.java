package com.onetool.server.api.chat.controller;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.dto.ChatMessageResponse;
import com.onetool.server.api.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "채팅", description = "채팅 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Operation(summary = "채팅방 조회 API", description = "특정 채팅방의 정보를 조회합니다.")
    @GetMapping("/chatroom")
    public ChatRoom getChatRoom(@RequestParam String roomId) {
        return chatService.findRoomById(roomId);
    }

    @Operation(summary = "전체 채팅방 목록 조회 API", description = "생성된 모든 채팅방의 목록을 조회합니다.")
    @GetMapping("/chatroom/list")
    public List<ChatRoom> getChatRoomList(){
        return chatService.findAllRoom();
    }

    @Operation(summary = "채팅방 생성 API", description = "새로운 채팅방을 생성합니다.")
    @PostMapping("/chatroom")
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @Operation(summary = "채팅 메시지 목록 조회 API", description = "특정 채팅방의 최근 메시지 목록을 조회합니다.")
    @GetMapping("/list")
    public List<ChatMessageResponse> getChatMessages(@RequestParam String roomId) {
        List<ChatMessage> chatMessages = chatService.findLatestMessages(roomId);
        return chatMessages.stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessage chatMessage) {
        chatService.saveMessage(chatMessage);
    }
}
