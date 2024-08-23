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
    private static final String API_KEY2 = "sk-Qe6CeNA94XBHRhCK886bMyxDnZpKPKOecotjtMJRi0T3BlbkFJa6jt1ty4FjOWx_6dmaitFbTtpOmyksHYrIM3ASGocA";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String sendMessageToGPT(String userMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();
        int retryCount = 0;
        int maxRetries = 5;
        int backoffTime = 1000; // 시작 지연 시간 (밀리초)

        Map<String, Object> json = new HashMap<>();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", List.of(Map.of("role", "user", "content", userMessage)));
while (retryCount < maxRetries) {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), new ObjectMapper().writeValueAsString(json));

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .header("Authorization", "Bearer " + API_KEY2)
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
        }else if (response.code() == 429){
            // 429 Too Many Requests 오류 처리
            retryCount++;
            System.out.println("429 오류 발생, " + backoffTime + "ms 후 재시도...");
            try {
                Thread.sleep(backoffTime); // InterruptedException 처리
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 설정
                throw new IOException("Thread interrupted during backoff", e);
            }
            backoffTime *= 2; // 백오프 시간 증가
        }else {
            throw new IOException("OpenAI API 요청 실패: " + response.code());
        }
    }
        throw new IOException("최대 재시도 횟수를 초과했습니다.");
    }
}
