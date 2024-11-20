package com.nurijang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nurijang.dto.request.UserMessageRequest;
import com.nurijang.service.GeminiChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GeminiController {
    private final GeminiChatbotService chatbotService;

    @PostMapping("/chat")
    public String chatWithBot(@RequestBody UserMessageRequest userMessage) throws JsonProcessingException {
        return chatbotService.getChatbotResponse(userMessage.getMessage());
    }
}
