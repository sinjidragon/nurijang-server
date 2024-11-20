package com.nurijang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nurijang.dto.request.UserMessageRequest;
import com.nurijang.service.GeminiChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat Bot", description = "챗봇 ai")
@RestController
@RequiredArgsConstructor
public class GeminiController {
    private final GeminiChatbotService chatbotService;

    @Operation(summary = "chatbot", description = "prompt 입력")
    @PostMapping("/chat")
    public String chatWithBot(@RequestBody UserMessageRequest userMessage) throws JsonProcessingException {
        return chatbotService.getChatbotResponse(userMessage.getMessage());
    }
}
