package com.nurijang.controller;

import com.nurijang.dto.request.PromptRequest;
import com.nurijang.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Chat", description = "챗봇 관련 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenAIService openAIService;

    @Operation(summary = "챗봇 시작하기 (threadId 발급받기)")
    @PostMapping("/start")
    public String startChat() throws IOException {
        return openAIService.createThread();
    }

    @Operation(summary = "prompt 보내기")
    @PostMapping("/send")
    public String chat(@RequestBody PromptRequest request) throws IOException {
        return openAIService.sendMessageAndRun(request.getThreadId(), request.getPrompt());
    }
}
