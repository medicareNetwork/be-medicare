package be.com.bemedicare.board.controller;

import be.com.bemedicare.board.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Map<String, String> request) {
        try {
            String userMessage = request.get("message");
            return openAIService.sendMessageToGPT(userMessage);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
