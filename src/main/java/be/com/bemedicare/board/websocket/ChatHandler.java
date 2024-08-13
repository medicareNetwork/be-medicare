package be.com.bemedicare.board.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChatHandler extends TextWebSocketHandler {

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions"; // gpt-3.5-turbo 엔드포인트
    private static final String API_KEY = "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"; // 여기에 GPT API 키를 설정하세요
    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userMessage = message.getPayload();
        try {
            String gptResponse = getGptResponse(userMessage);
            session.sendMessage(new TextMessage(gptResponse));
        } catch (HttpClientErrorException e) {
            logger.error("API call error: {}", e.getMessage());
            session.sendMessage(new TextMessage("Error processing your request. Please try again later."));
        }
    }

    private String getGptResponse(String message) throws IOException {
        // 요청 JSON 형식
        String requestJson = "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}],\"max_tokens\":50}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = null;

        // 요청 빈도 조절 (예: 1초 대기)
        try {
            response = restTemplate.exchange(GPT_API_URL, HttpMethod.POST, request, String.class);
            TimeUnit.SECONDS.sleep(1); // 1초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting between requests", e);
        }

        // GPT 응답에서 텍스트만 추출합니다.
        return extractTextFromResponse(response.getBody());
    }

    private String extractTextFromResponse(String responseBody) {
        // GPT 응답에서 생성된 텍스트를 추출합니다.
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode messageNode = root.path("choices").get(0).path("message").path("content");
            return messageNode.asText();
        } catch (Exception e) {
            logger.error("Error extracting text from response", e);
            return "Error processing response";
        }
    }
}
