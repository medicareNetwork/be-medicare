package be.com.bemedicare.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;
    private static final String API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String sendMessageToGPT(String userMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", List.of(Map.of("role", "user", "content", userMessage)));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), new ObjectMapper().writeValueAsString(json));

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            Map<String, Object> responseJson = new ObjectMapper().readValue(responseBody, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseJson.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        } else {
            throw new IOException("OpenAI API 요청 실패: " + response.code());
        }
        return "API 응답이 없습니다.";
    }
}
