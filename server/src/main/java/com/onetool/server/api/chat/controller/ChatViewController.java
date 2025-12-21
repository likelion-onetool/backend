package com.onetool.server.api.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {

    @GetMapping("/chat/test")
    public String chatTest() {
        return "chat-test";
    }
}
