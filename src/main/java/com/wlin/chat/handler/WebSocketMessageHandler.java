package com.wlin.chat.handler;

import com.wlin.chat.model.WebSocketReceiveMessage;

public interface WebSocketMessageHandler {
    boolean supports(WebSocketReceiveMessage webSocketReceiveMessage);

    void handleMessage();
}
