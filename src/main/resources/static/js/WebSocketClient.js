var socket = new WebSocket("ws://localhost:8090/ws/chat");

socket.onopen = function(event) {
    console.log("WebSocket is open now.");
};

socket.onmessage = function(event) {
    console.log("Message from server ", event.data);
};

socket.onclose = function(event) {
    console.log("WebSocket is closed now.");
};

function sendMessage(message) {
    socket.send(message);
}
