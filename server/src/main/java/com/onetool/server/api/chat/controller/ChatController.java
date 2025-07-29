package com.onetool.server.api.chat.controller;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.dto.ChatMessageResponse;
import com.onetool.server.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chatroom")
    public ChatRoom getChatRoom(@RequestParam String roomId) {
        return chatService.findRoomById(roomId);
    }

    @GetMapping("/chatroom/list")
    public List<ChatRoom> getChatRoomList(){
        return chatService.findAllRoom();
    }

    @PostMapping("/chatroom")
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping("/chat/list")
    public List<ChatMessageResponse> getChatMessages(Pageable pageable, @RequestParam String roomId) {
        return chatService.findChatMessages(pageable, roomId);
    }



    @Scheduled(cron = "0 32 9 * * *")
    public void deleteExpiredChatMessagesScheduled() {
        chatService.deleteExpiredChatMessages();
    }
}
