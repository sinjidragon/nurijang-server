package com.nurijang.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.api-url}")
    private String apiUrl;

    @Value("${spring.ai.openai.assistantId}")
    private String assistantId;


    public String createThread() throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(apiUrl);
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("OpenAI-Beta", "assistants=v1");

            try (CloseableHttpResponse response = client.execute(post)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String sendMessageAndRun(String threadId, String prompt) throws IOException{
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String messageResponse = sendMessage(client, threadId, prompt);
            String runResponse = runAssistant(client, threadId);
            Thread.sleep(2000);
            String getMessage = getMessage(threadId);
            return getMessage;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendMessage(CloseableHttpClient client, String threadId, String prompt) throws IOException {
        HttpPost post = new HttpPost(apiUrl + "/" + threadId + "/messages");
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("OpenAI-Beta", "assistants=v2");

        String json = objectMapper.writeValueAsString(Map.of("role", "user", "content", prompt));
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = client.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    private String runAssistant(CloseableHttpClient client, String threadId) throws IOException {
        HttpPost post = new HttpPost(apiUrl + "/" + threadId + "/runs");
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("OpenAI-Beta", "assistants=v2");

        String json = objectMapper.writeValueAsString(Map.of("assistant_id", assistantId));
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = client.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public String getMessage(String threadId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("OpenAI-Beta", "assistants=v2");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        int i = 0;

        while (true) {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl + "/" + threadId + "/messages",
                    HttpMethod.GET,
                    request,
                    String.class
            );

            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode dataArray = rootNode.path("data");

                log.info("요청 반복 횟수" + i);
                String role = dataArray.get(0).path("role").asText();
                if (!"user".equals(role)) {
                    JsonNode firstMessageContent = dataArray.get(0).path("content");
                    if (firstMessageContent.isArray() && firstMessageContent.size() > 0) {
                        return firstMessageContent.get(0).path("text").path("value").asText();
                    }
                }

                Thread.sleep(1000);
                i += 1;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}