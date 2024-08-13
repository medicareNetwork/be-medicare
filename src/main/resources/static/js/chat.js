document.addEventListener("DOMContentLoaded", function() {
    const chatBox = document.getElementById("chat-box");
    const messageInput = document.getElementById("message-input");
    const sendButton = document.getElementById("send-button");

    const socket = new WebSocket("ws://localhost:8080/chat");

    socket.onmessage = function(event) {
        const message = document.createElement("div");
        message.textContent = "AI: " + event.data;
        chatBox.appendChild(message);
        chatBox.scrollTop = chatBox.scrollHeight; // 스크롤을 항상 최신 메시지로 이동
    };

    sendButton.addEventListener("click", function() {
        const message = messageInput.value;
        if (message.trim()) {
            const userMessage = document.createElement("div");
            userMessage.textContent = "User: " + message;
            chatBox.appendChild(userMessage);
            chatBox.scrollTop = chatBox.scrollHeight;
            socket.send(message);
            messageInput.value = "";
        }
    });
});
